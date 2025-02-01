package com.laborlink.renting.dtos;

import lombok.Data;

@Data
public class SingleRentedItemDTO {

    private Long toolId;
    
    private Integer quantity;

    private Double pricePerSecond;
 
}
