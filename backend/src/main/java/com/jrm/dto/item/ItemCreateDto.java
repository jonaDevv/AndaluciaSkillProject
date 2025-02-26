package com.jrm.dto.item;


import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class ItemCreateDto {

    @Schema(description = "Descripción del item", example = "Evaluación de matrices", required = true)
    @NotNull(message = "La descripcion no puede ser nula")
    private String description;

    @NotNull
    @Positive(message = "El valor debe ser positivo")
    private int weight;

    @DecimalMin(value = "0.0", message = "La valoracion no puede ser negativa")
    private float percentage;

    @Schema(description = "ID de la prueba relacionada", example = "1", required = true)
    @NotNull(message = "El test al que pertenece no puede ser nulo")
    private Long prueba;


}
