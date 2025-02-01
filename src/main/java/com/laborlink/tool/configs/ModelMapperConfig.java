package com.laborlink.tool.configs;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.laborlink.tool.dtos.ToolDTO;
import com.laborlink.tool.entities.Tool;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper ret = new ModelMapper();

        // Mapping ToolDTO into Tool Entity
        ret.addMappings(new PropertyMap<ToolDTO,Tool>() {
            @Override
            protected void configure() {
                skip(destination.getId());
                map(source.getToolName(), destination.getName());
                map(source.getToolStatus(),destination.getStatus());
                map(source.getPricePerSecond(),destination.getPricePerSec());
            }
        });


        ret.addMappings(new PropertyMap<Tool,ToolDTO>() {
        @Override
        protected void configure() {
            map(source.getName(),destination.getToolName());
            map(source.getStatus(),destination.getToolStatus());
            map(source.getPricePerSec(),destination.getPricePerSecond());
        }
        });
  

        return ret;
    }
    
}
