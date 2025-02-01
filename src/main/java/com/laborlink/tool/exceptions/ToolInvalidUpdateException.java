package com.laborlink.tool.exceptions;

public class ToolInvalidUpdateException extends RuntimeException {

    public ToolInvalidUpdateException(Long toolId,String msg){
        super(String.format("Invalid to update tool with id %d. %s", toolId, msg));
    }
    
}
