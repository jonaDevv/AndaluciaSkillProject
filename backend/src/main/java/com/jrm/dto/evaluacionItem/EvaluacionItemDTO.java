package com.jrm.dto.evaluacionItem;

import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class EvaluacionItemDTO {
    private Long id;
    private String description;

    @Schema(description = "Valoración técnica", minimum = "0", maximum = "100", example = "85")
    private Float valoracion;
    private String justificacion;
    private Long itemId;
}
