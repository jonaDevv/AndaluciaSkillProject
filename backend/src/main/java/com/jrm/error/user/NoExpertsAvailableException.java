package com.jrm.error.user;

public class NoExpertsAvailableException  extends RuntimeException {

    public NoExpertsAvailableException(Long specialtyId) {
        super("No hay expertos disponibles para la especialidad " + specialtyId);
    }

}
