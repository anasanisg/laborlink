package com.laborlink.renting.exceptions;

public class UserHasAnExistedContract extends RuntimeException {

    public UserHasAnExistedContract(String Id){
        super(String.format("The user with %s has a currently running renting contract. the user can not has more than one contract per time",Id));
    }
    
}
