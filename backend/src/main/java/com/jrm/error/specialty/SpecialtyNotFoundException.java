package com.jrm.error.specialty;



public class SpecialtyNotFoundException extends RuntimeException {

	
	
	public SpecialtyNotFoundException(Long id) {
		super("No se encuentra la especialidad con id: " + id);
	}

	

}
