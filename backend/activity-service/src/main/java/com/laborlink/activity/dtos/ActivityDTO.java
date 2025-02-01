package com.laborlink.activity.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.laborlink.activity.enums.ActivityStatus;

import lombok.Data;

@Data
public class ActivityDTO {

    private String activityId;

    private String ownerId;

    private List<SingleRentedItemDTO> items;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMMM yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    private Long totalRentingTimeInSeconds;

    private Double totalPrice;

    private ActivityStatus status;

    
}
