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

    
    @NotNull(message = "La descripción no puede ser nula")
    private String description;

    @DecimalMin(value = "0.0", message = "El peso no puede ser negativo")
    private int weight;

    @DecimalMin(value = "0.0", message = "La valoración no puede ser negativa")
    private float percentage;

    @NotNull(message = "El test al que pertenece no puede ser nulo")
    private Long prueba;

}
