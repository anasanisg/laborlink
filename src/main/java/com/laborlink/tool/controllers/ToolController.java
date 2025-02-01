package com.laborlink.tool.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laborlink.tool.dtos.ResponseShape;
import com.laborlink.tool.dtos.ToolDTO;
import com.laborlink.tool.entities.Tool;
import com.laborlink.tool.services.ToolService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001", "http://localhost:8087"})
@RestController
@RequestMapping("/tool")
@AllArgsConstructor
public class ToolController {

    private ToolService toolService;
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ResponseShape> createTool(@RequestBody ToolDTO tool) {

        ToolDTO response = modelMapper.map(toolService.createTool(tool), ToolDTO.class);

        return new ResponseEntity<>(new ResponseShape(response), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseShape> getAllTools() {

        List<ToolDTO> response = toolService.getAllTools().stream()
                .map(tool -> modelMapper.map(tool, ToolDTO.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseShape(response),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseShape> getToolById(@PathVariable Long id) {
        ToolDTO response = modelMapper.map(toolService.getToolById(id), ToolDTO.class);
        return new ResponseEntity<>(new ResponseShape(response),
                HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseShape> updateToolStatus(@PathVariable Long id,
            @RequestBody Map<String, Object> values) {

        ToolDTO response = modelMapper.map(toolService.updateTool(id, values), ToolDTO.class);

        return new ResponseEntity<>(new ResponseShape(response),
                HttpStatus.OK);
    }



    /*
     * 
     * 
     * Here are Internal Service Communication Just 
     * between the ToolService(LaborL Laager & Labor Renting Machine)
     * This Communication over HTTP protocoll with Reactive Web Flux
     * 
     * 
     * 
     * 
     */

    @PostMapping("/get-group-of-tools")
    public Mono<List<Tool>> getToolsByIds(@RequestBody List<Long> ids) {
        // Delegate to the service and return a reactive Mono
        return toolService.getListOfToolsByListOfIds(ids);
    }


    @PostMapping("/update-group-of-tools")
    public void updateCollectOfTools(@RequestBody List<Tool> tools) {
        toolService.updateCollectOfTools(tools);
    }

}
