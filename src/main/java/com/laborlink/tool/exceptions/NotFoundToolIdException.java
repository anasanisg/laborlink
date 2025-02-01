package com.laborlink.tool.exceptions;

public class NotFoundToolIdException extends RuntimeException {
    
    public NotFoundToolIdException(String data) {
        super(String.format("The Tool with Id %s not found",
                data));
    }


}
