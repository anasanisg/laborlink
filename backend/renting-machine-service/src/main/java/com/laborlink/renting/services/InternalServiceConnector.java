package com.laborlink.renting.services;

import java.util.List;

import com.laborlink.renting.dtos.internal.Tool;

public interface InternalServiceConnector {

    List<Tool> loadGroupOfToolByGroupOfIds(List<Long> ids);

    void updateGroupOfTools(List<Tool> tools);
    
}
