package com.jrm.dto.item;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class ItemUpdateDto {

    
    private Long id;

   
    private String description;

    
    @Positive(message = "El valor debe ser positivo")
    private int weight;

    @DecimalMin(value = "0.0", message = "La valoracion no puede ser negativa")
    private float percentage;

 
    private Long prueba;

}
