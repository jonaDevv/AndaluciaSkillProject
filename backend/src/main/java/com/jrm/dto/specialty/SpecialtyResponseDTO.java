// SpecialtyResponseDTO.java
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
public class SpecialtyResponseDTO {
    private Long id;
    private String cod;
    private String name;
}