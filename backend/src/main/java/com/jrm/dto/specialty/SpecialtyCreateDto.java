// SpecialtyCreateDTO.java
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
public class SpecialtyCreateDto {

    @Schema(description = "Código único de la especialidad", example = "MAT", required = true)
    @NotNull(message = "El código no puede ser nulo")
    private String cod;

    @Schema(description = "Nombre de la especialidad", example = "Matemáticas Avanzadas", required = true)
    @NotNull(message = "El nombre no puede ser nulo")
    private String name;
}