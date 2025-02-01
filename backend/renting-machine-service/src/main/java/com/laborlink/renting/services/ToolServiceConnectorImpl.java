package com.laborlink.renting.services;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.laborlink.renting.dtos.internal.Tool;
import com.laborlink.renting.exceptions.UnkownGlobalException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ToolServiceConnectorImpl implements InternalServiceConnector {

    private final WebClient webClient; 

    @Override
    public List<Tool> loadGroupOfToolByGroupOfIds(List<Long> ids) {
               try {
            return webClient.post()
                    .uri("/tool/get-group-of-tools")
                    .bodyValue(ids)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Tool>>() {
                    })
                    .block();
        } catch (Exception e) {
            throw new UnkownGlobalException();
        }
    }

    @Override
    public void updateGroupOfTools(List<Tool> tools) {
        try {
            webClient.post()
                    .uri("/tool/update-group-of-tools")
                    .bodyValue(tools)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            throw new UnkownGlobalException();
        }
    }
    
}
