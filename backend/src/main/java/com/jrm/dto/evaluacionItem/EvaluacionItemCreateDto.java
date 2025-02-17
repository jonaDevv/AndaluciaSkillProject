package com.jrm.dto.evaluacionItem;

import com.jrm.model.Evaluacion;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;

public class EvaluacionItemCreateDto {

    
    @DecimalMin(value = "0.0", message = "La valoracion no puede ser negativa")
    private String valoracion;

    @Pattern(regexp = "^[a-zA-Z0-9]+([.,][0-9]+)?$", message = "La justificacion debe ser un alfanumérico")
    private String justificacion;

    @ManyToOne
    @JoinColumn(name = "evaluacion_id") 
    private Evaluacion evaluacion;

   
    private Long item;

}
