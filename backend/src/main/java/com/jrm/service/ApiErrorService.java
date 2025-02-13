package com.jrm.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.jrm.error.ApiError;

@Service
public class ApiErrorService {

    
    public static ApiError getErrorMessage(String message) {
        
        ApiError error = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .menssage(message)
                .build();
        
        return error;
    }
}
