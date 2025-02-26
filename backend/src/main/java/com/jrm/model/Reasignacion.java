package com.jrm.model;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;

@Embeddable
public class Reasignacion {
    private Long expertoAnterior;
    private Long expertoNuevo;
    private LocalDateTime fecha;
}