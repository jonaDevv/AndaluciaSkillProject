// SpecialtyUpdateDTO.java
package com.jrm.dto.specialty;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class SpecialtyUpdateDTO {
    @NotNull(message = "El código no puede ser nulo")
    private String cod;

    @NotNull(message = "El nombre no puede ser nulo")
    private String name;
}