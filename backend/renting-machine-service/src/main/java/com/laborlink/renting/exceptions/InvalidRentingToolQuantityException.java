package com.laborlink.renting.exceptions;

public class InvalidRentingToolQuantityException extends RuntimeException {

    public InvalidRentingToolQuantityException(Long id){
        super(String.format("The quantity of the tool with %s that you have asked are not available at this moment.",id));
    }
    
}
