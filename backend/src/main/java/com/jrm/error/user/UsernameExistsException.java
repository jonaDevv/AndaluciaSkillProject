package com.jrm.error.user;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UsernameExistsException  extends RuntimeException {

    public UsernameExistsException(String message) {
        super("El usuario ya existe");
    }

  
}
