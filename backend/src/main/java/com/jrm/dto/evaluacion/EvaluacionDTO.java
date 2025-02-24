package com.jrm.dto.evaluacion;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class EvaluacionDTO {

    private Long id;
    private String estado;
    private float pFinalObtenida;
    private float porcentajeFinalObtenido;
    
    // Datos del participant
    private Long participantId;
    private String participantName;
    
    // Datos de la prueba
    private Long pruebaId;
    private String pruebaEnunciado;
    
    // Datos del experto
    private Long userId;
    private String userUsername;
}
