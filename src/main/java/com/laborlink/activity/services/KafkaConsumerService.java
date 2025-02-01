package com.laborlink.activity.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laborlink.activity.dtos.ActivityDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class KafkaConsumerService {

    private ActivityService activityService;

    @KafkaListener(topics = "activity.record.events")
    public void recieveRentingActivities(String msg) {

        System.out.println(String.format("CONSUMED MSG : %s",msg));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            activityService.record(objectMapper.readValue(msg, ActivityDTO.class));     
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @KafkaListener(topics = "activity.complete.events")
    public void recieveCompletedActivities(String msg) {

        System.out.println(String.format("CONSUMED MSG : %s",msg));

        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  
        try {
            activityService.record(objectMapper.readValue(msg, ActivityDTO.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
