
package com.jrm.dto.evaluacion;

import org.springframework.validation.annotation.Validated;


import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class EvaluacionUpdateDto {


    @DecimalMin(value = "0.0", message = "La valoracion no puede ser negativa")
    private String pFinalObtenida;

    
    // private User user;

   
    // private Participant participant;

    
    // private Prueba prueba;

}
