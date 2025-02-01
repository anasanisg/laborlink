package com.laborlink.activity.dtos;

import lombok.Data;

@Data
public class SingleRentedItemDTO {
    
    private Long toolId;

    private Integer quantity;

    private Double pricePerSecond;
    
}
