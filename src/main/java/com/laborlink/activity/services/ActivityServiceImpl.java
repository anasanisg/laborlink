package com.laborlink.activity.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.laborlink.activity.Repositories.ActivityRepo;
import com.laborlink.activity.configs.ModelMapperConfig;
import com.laborlink.activity.dtos.ActivityDTO;
import com.laborlink.activity.entities.Activity;
import com.laborlink.activity.entities.RentedTool;
import com.laborlink.activity.entities.ReturnedTool;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private ActivityRepo activityRepo;
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public void record(ActivityDTO activity) {

        Optional<Activity> ret = activityRepo.findById(activity.getActivityId());

        /*
         * If the Activity not exist this means two thing
         * - The Activity is renting and with musst be with pending status
         * - The Source of activity is activity.record.events
         */
        if (!ret.isPresent()) {
            Activity record = modelMapper.map(activity, Activity.class);

            // This converted will handle injecting the activity (ManyToOne JPA API) to
            // RentedToolEntity through mapping
            modelMapper.addConverter(ModelMapperConfig.sDTOdRentedToolEntity(record));

            // The Converter will be appliead out of the box thanks to ModelMapper
            List<RentedTool> rentedTools = activity.getItems()
                    .stream()
                    .map(tool -> modelMapper.map(tool, RentedTool.class))
                    .collect(Collectors.toList());

            record.setRentedTools(rentedTools);

            activityRepo.save(record);

        } else { // Activity exist means this is return activity with status could be (COMPLETED,
                 // TO_BE_REVIEWED) Source activity.complete.events
            Activity record = ret.get();
            
            record.setStatus(activity.getStatus());
            record.setTotalPrice(activity.getTotalPrice());
            

            modelMapper.addConverter(ModelMapperConfig.sDTOdReturnedToolEntity(record));

            List<ReturnedTool> returnedTools = activity.getItems()
                    .stream()
                    .map(tool -> modelMapper.map(tool, ReturnedTool.class))
                    .collect(Collectors.toList());

            if (record.getReturnedTools() == null) {
                record.setReturnedTools(new ArrayList<>());
            }

            // Update Collection Safely
            record.getReturnedTools().addAll(returnedTools);

            activityRepo.save(record);
        }

    }

}
