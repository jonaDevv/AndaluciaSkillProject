// SpecialtyResponseDTO.java
package com.jrm.dto.specialty;

import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "DTO de respuesta para especialidades")
public class SpecialtyResponseDTO {
    
    @Schema(description = "ID de la especialidad", example = "1")
    private Long id;
    
    @Schema(description = "Código único", example = "MAT-01")
    private String cod;
    
    @Schema(description = "Nombre de la especialidad", example = "Matemáticas Avanzadas")
    private String name;
}