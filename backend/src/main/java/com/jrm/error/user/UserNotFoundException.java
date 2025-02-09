package com.jrm.error.user;

import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("No se ha encontrado el usuario con id: " + id);
    }

}
