package com.laborlink.tool.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.laborlink.tool.dtos.ToolDTO;
import com.laborlink.tool.entities.Tool;
import com.laborlink.tool.exceptions.NotFoundToolIdException;
import com.laborlink.tool.exceptions.ToolInvalidUpdateException;
import com.laborlink.tool.exceptions.ToolNameIsExistedException;
import com.laborlink.tool.repositories.ToolRepo;
import com.laborlink.tool.utils.EnumConverter;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ToolServiceImpl implements ToolService {

    private ToolRepo toolRepo;
    private ModelMapper modelMapper;

    @Override
    public Tool createTool(ToolDTO tool) {

        if (isToolNameExisted(tool.getToolName()))
            throw new ToolNameIsExistedException(tool.getToolName());

        return toolRepo.save(modelMapper.map(tool, Tool.class));
    }

    @Override
    public Tool getToolById(Long toolId) {

        Optional<Tool> tool = toolRepo.findById(toolId);

        if (!tool.isPresent())
            throw new NotFoundToolIdException(toolId.toString());

        return tool.get();
    }

    @Override
    public List<Tool> getAllTools() {
        return (List<Tool>) toolRepo.findAll();
    }

    @Override
    public Tool updateTool(Long id, Map<String, Object> values) {

        Tool tool = getToolById(id);

        try {

            for (Map.Entry<String, Object> item : values.entrySet()) {

                if (item.getValue() == null)
                    continue;

                // Img
                if (item.getKey() == "img" && item.getValue() != tool.getImg()) {
                    tool.setImg((String) item.getValue());
                    continue;
                }

                // Description
                if (item.getKey() == "description" && item.getValue() != tool.getDescription()) {
                    tool.setDescription((String) item.getValue());
                    continue;
                }

                // Price
                if (item.getKey() == "pricePerMinute" && item.getValue() != tool.getPricePerSec()) {
                    tool.setPricePerSec(((Number) item.getValue()).doubleValue());
                    continue;
                }

                // Quantity
                if (item.getKey() == "quantity" && item.getValue() != tool.getQuantity()) {
                    tool.setQuantity((Integer) item.getValue());
                    continue;
                }

                // Status
                if (item.getKey() == "toolStatus" && EnumConverter.convertToToolStatus(tool.getId().toString(),
                        item.getValue()) != tool.getStatus()) {
                    tool.setStatus(EnumConverter.convertToToolStatus(tool.getId().toString(), item.getValue()));
                    continue;
                }

            }

        } catch (Exception e) {

            if (e instanceof ClassCastException)
                throw new ToolInvalidUpdateException(tool.getId(), "One or more value has invalid type");

            throw new ToolInvalidUpdateException(tool.getId(), "");

        }

        return toolRepo.save(tool);
    }

    private Boolean isToolNameExisted(String toolName) {
        return toolRepo.findByName(toolName).isPresent();
    }

@Override
public Mono<List<Tool>> getListOfToolsByListOfIds(List<Long> ids) {
    return Mono.fromCallable(() -> 
        StreamSupport.stream(toolRepo.findAllById(ids).spliterator(), false)
                     .collect(Collectors.toList())
    );
}

@Override
public void updateCollectOfTools(List<Tool> tool) {
   toolRepo.saveAll(tool);
}

}
