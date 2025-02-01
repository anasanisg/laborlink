package com.laborlink.renting.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laborlink.renting.dtos.ActivityDTO;
import com.laborlink.renting.enums.LaborLinkTopics;
import com.laborlink.renting.exceptions.UnkownGlobalException;
import com.laborlink.renting.utils.EnumConverter;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    @Override
    public void send(LaborLinkTopics topic, ActivityDTO activity) {

        try {

            /*
             * IMPORTANT NOTE tool.rental.events
             * This Topic follow kafka Log Compact Rentention Policy
             * This Topic contains two Related Actions (Rent Tools, and Return Tools)
             * Both Messages are compacted with unique UUID which is Activity Id
             */
            // Publish to tool.rental.events
            if (topic == LaborLinkTopics.ToolRentalEvents)
                kafkaTemplate.send(EnumConverter.getTopicName(topic), activity.getActivityId(),
                        objectMapper.writeValueAsString(activity));

            // Pulbish to activity.record.events
            if (topic == LaborLinkTopics.ActivityRecordEvents)
                kafkaTemplate.send(EnumConverter.getTopicName(topic), objectMapper.writeValueAsString(activity));

        } catch (Exception e) {
            throw new UnkownGlobalException();
        }

    }

}
