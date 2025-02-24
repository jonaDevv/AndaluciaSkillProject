package com.jrm.dto.evaluacionItem;

import org.springframework.validation.annotation.Validated;

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
    private Float valoracion;
    private String justificacion;
    private Long itemId;
}
