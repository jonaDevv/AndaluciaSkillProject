package com.jrm.error.evaluacionitem;

public class EvaluacionItemNotFoundException  extends RuntimeException {

    public EvaluacionItemNotFoundException(Long id) {
        super("No se ha encontrado la evaluación item con id: " + id);
    }

}
