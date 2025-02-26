package com.jrm.dto.evaluacion;
import org.springframework.validation.annotation.Validated;

import com.jrm.model.Participant;
import com.jrm.model.Prueba;
import com.jrm.model.User;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder 
public class EvaluacionResponseDto {

    private Long id;

    private float pFinalObtenida;

    private float porcentajeFinalObtenido;

    
    private long user;

   
    private long participant;

    
    private long prueba;

    
}
