package com.laborlink.renting.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.laborlink.renting.dtos.ActivityDTO;
import com.laborlink.renting.dtos.SingleRentedItemDTO;
import com.laborlink.renting.dtos.internal.Tool;
import com.laborlink.renting.entities.Contract;
import com.laborlink.renting.enums.ActivityStatus;
import com.laborlink.renting.enums.LaborLinkTopics;
import com.laborlink.renting.exceptions.InvalidRentingToolIdException;
import com.laborlink.renting.exceptions.InvalidRentingToolQuantityException;
import com.laborlink.renting.exceptions.MissedContractException;
import com.laborlink.renting.exceptions.UserHasAnExistedContract;
import com.laborlink.renting.repositories.ContractRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private PublisherService publisherService;
    private ContractRepo contractRepo;
    private InternalServiceConnector internalServiceConnector;

    @Override
    public void startRentingToolsActivity(List<SingleRentedItemDTO> items,String userId) {

        // Step * : Check if the user has an existed contract

 

        Optional<Contract> contract = contractRepo.findByUserId(userId);
        if (contract.isPresent())
            throw new UserHasAnExistedContract(userId);

        // Step 1: Fetching Tools and validate the existance of the Lisz
        List<Long> ids = items.stream()
                .map(SingleRentedItemDTO::getToolId)
                .collect(Collectors.toList());

        if (ids.isEmpty())
            throw new InvalidRentingToolIdException();

        /*
         * Here are internal communication using webflux
         * with the tool service to load a bunch of tools thats already existed in the
         * lage
         * SERVICE TO SERVICE COMMUNICATION through HTTP Protocol
         */

        List<Tool> tools = internalServiceConnector.loadGroupOfToolByGroupOfIds(ids);

        List<Long> existedIds = tools.stream()
                .map(Tool::getId)
                .collect(Collectors.toList());

        List<Long> missedIds = ids.stream()
                .filter(id -> !existedIds.contains(id))
                .collect(Collectors.toList());

        if (!missedIds.isEmpty())
            throw new InvalidRentingToolIdException(missedIds);

        // Step 2 : Update Tools Quantity

        tools.forEach(tool -> {
            items.stream()
                    .filter(item -> item.getToolId().equals(tool.getId()))
                    .findFirst()
                    .ifPresent(dto -> {
                        int updatedQuantity = tool.getQuantity() - dto.getQuantity();

                        if (updatedQuantity < 0)
                            throw new InvalidRentingToolQuantityException(tool.getId());

                        tool.setQuantity(updatedQuantity);
                    });
        });

        // Step 3 Update Quantity in Tools Table and Register new temp Contract Tables
        String activityId = UUID.randomUUID().toString();

        // Communicate with ToolService to Update tools quantities throguh reactive
        // webflux
        internalServiceConnector.updateGroupOfTools(tools);


        contractRepo.save(new Contract(userId, activityId));

        // Step 4 Publisch rentStarted Event to Kafka
        ActivityDTO currentActivity = new ActivityDTO(
                activityId,
                userId,
                items,
                ActivityStatus.PENDING);

        publisherService.send(LaborLinkTopics.ToolRentalEvents, currentActivity);
        publisherService.send(LaborLinkTopics.ActivityRecordEvents, currentActivity);

    }

    @Override
    public void startReturnToolsActivity(List<SingleRentedItemDTO> items,String userId) {

        // Step * Get the temproray contract related to the userId

        Optional<Contract> tempContract = contractRepo.findByUserId(userId);

        if (!tempContract.isPresent())
            throw new MissedContractException(userId);

        // Step 1 Fetching Tools
        List<Long> ids = items.stream()
                .map(SingleRentedItemDTO::getToolId)
                .collect(Collectors.toList());

        // Load the tools from tool service through HTTP protocol
        List<Tool> tools = internalServiceConnector.loadGroupOfToolByGroupOfIds(ids);

        // Step 2 Update Quantity

        tools.forEach(tool -> {
            items.stream()
                    .filter(item -> item.getToolId().equals(tool.getId()))
                    .findFirst()
                    .ifPresent(dto -> {

                        int updatedQuantity = tool.getQuantity() + dto.getQuantity();

                        tool.setQuantity(updatedQuantity);
                    });
        });

        // Step 3 Update Quantity in Tools table & Delete the Temp Contract
        internalServiceConnector.updateGroupOfTools(tools); // tell the tool service to update quantities

        contractRepo.delete(tempContract.get());

        // Step 4 Publish rentingEnded
        ActivityDTO currentActivityDTO = new ActivityDTO(tempContract.get().getActivityId(), userId, items,
                ActivityStatus.PENDING);
        publisherService.send(LaborLinkTopics.ToolRentalEvents, currentActivityDTO);

    }

}
