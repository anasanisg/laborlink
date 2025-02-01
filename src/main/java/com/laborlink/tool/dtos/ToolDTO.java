package com.laborlink.tool.dtos;

import com.laborlink.tool.enums.ToolStatus;

import lombok.Data;

@Data
public class ToolDTO {

    private Long Id;

    private String toolName;
    
    private String description;

    private String img;

    private Double pricePerSecond;

    private Integer quantity;

    private ToolStatus toolStatus;
    
}
