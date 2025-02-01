package com.laborlink.renting.dtos;

import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Setter;


@Setter
public class ResponseShape {
    

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMMM yyyy HH:mm:ss") // Setting Default Pattern for Time
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object response; 

    public ResponseShape(Object response){
        this.timestamp = LocalDateTime.now();
        this.response = response;
    }
    
    public Object getResponse() {
        return response;
    }

}
