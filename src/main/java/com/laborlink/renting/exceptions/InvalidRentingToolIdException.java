package com.laborlink.renting.exceptions;

import java.util.List;

public class InvalidRentingToolIdException extends RuntimeException{

    public InvalidRentingToolIdException(){
        super(String.format("Invalid Ids no tool is found"));
    }

    public InvalidRentingToolIdException(List<Long> missed){
        super(String.format("Invalid tool ids, the tools with the id %s not existed",missed));
    }

}
