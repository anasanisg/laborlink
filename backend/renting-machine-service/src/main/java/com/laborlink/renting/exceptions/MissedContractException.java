package com.laborlink.renting.exceptions;

public class MissedContractException extends RuntimeException {
    public MissedContractException(String id){
        super(String.format("This Exception should notifiy IT System. User try to return tools but no available temporary contract assigend to the user with id %s",id));
    }  
}
