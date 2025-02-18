package com.jrm.error.evaluacion;

public class EvaluacionNotFoundException extends RuntimeException {

	public EvaluacionNotFoundException(Long id) {
		super("No se ha encontrado la evaluación con id: " + id);
	}
}
