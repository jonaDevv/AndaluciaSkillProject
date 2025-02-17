package com.jrm.error.prueba;

import org.springframework.web.bind.annotation.RestController;


public class PruebaNotFoundException extends RuntimeException {

    
	public PruebaNotFoundException(Long id) {
		super("No se encuentra la prueba con id: " + id);
	}

}
