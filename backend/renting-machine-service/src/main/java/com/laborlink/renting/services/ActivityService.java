package com.laborlink.renting.services;

import java.util.List;

import com.laborlink.renting.dtos.SingleRentedItemDTO;

public interface ActivityService {

    void startRentingToolsActivity(List<SingleRentedItemDTO> items,String userId);

    void startReturnToolsActivity(List<SingleRentedItemDTO> items,String userId);
     
} 