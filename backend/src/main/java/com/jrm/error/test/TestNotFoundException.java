package com.jrm.error.test;

import org.springframework.web.bind.annotation.RestController;


public class TestNotFoundException extends RuntimeException {

    
	public TestNotFoundException(Long id) {
		super("No se encuentra la prueba con id: " + id);
	}

}
