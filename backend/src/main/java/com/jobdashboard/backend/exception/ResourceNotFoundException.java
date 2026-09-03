package com.jobdashboard.backend.exception;

// NotFound 커스텀 예외
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
