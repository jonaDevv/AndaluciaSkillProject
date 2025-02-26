package com.jrm.error.participant;



public class ParticipantNotFoundException extends RuntimeException {

	public ParticipantNotFoundException(Long id) {
		super("No se encuentra el participante con id: " + id);
	}

}
