package com.laborlink.renting.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.laborlink.renting.enums.ActivityStatus;

import lombok.Data;

@Data
public class ActivityDTO {

    private String activityId;

    private String ownerId;

    private List<SingleRentedItemDTO> items;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMMM yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    private ActivityStatus status;

    public ActivityDTO(String activityId, String ownerId, List<SingleRentedItemDTO> items,ActivityStatus status) {

        this.activityId = activityId;

        this.ownerId = ownerId;

        this.items = items;

        this.createdAt = LocalDateTime.now();

        this.status = status;
    }

}
