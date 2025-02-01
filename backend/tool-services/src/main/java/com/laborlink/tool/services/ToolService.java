package com.laborlink.tool.services;

import java.util.List;
import java.util.Map;

import com.laborlink.tool.dtos.ToolDTO;
import com.laborlink.tool.entities.Tool;

import reactor.core.publisher.Mono;

public interface ToolService {
    Tool createTool(ToolDTO tool); // ROLES ADMIN
    Tool getToolById(Long toolId); // ROLES ADMIN, USER
    List<Tool> getAllTools(); // ROLES ADMIN, USER


    /*
     * 
     * Only Invoked through Renting Machine Service or gateway only with developer ROLE 
     * 
     */

    Tool updateTool(Long id,Map<String,Object> values); 
    Mono<List<Tool>> getListOfToolsByListOfIds(List<Long> ids); 
    void updateCollectOfTools(List<Tool> tool);
}