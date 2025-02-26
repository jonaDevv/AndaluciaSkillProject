package com.jrm.dto.evaluacionItem;

import org.springframework.validation.annotation.Validated;

import com.jrm.model.Evaluacion;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class EvaluacionItemUpdateDto {

    private Long id;

    private String description;


    @Schema(description = "Valoración técnica", minimum = "0", maximum = "100", example = "85")
    @DecimalMin(value = "0.0", message = "La valoracion no puede ser negativa")
    private float valoracion;


    @Schema(description = "Justificación de la valoración", example = "Dominio avanzado del tema")
    @Pattern(regexp = "^[a-zA-Z0-9]+([.,][0-9]+)?$", message = "La justificacion debe ser un alfanumérico")
    private String justificacion;

   
    private Long evaluacionId;

   @NotNull(message = "El item no puede ser nulo")
    private Long item;

}
