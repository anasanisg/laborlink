package com.laborlink.renting.services;

import com.laborlink.renting.dtos.ActivityDTO;
import com.laborlink.renting.enums.LaborLinkTopics;

public interface PublisherService {

    void send(LaborLinkTopics topic,ActivityDTO activity);
  
} 