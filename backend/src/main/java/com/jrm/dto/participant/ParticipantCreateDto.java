package com.jrm.dto.participant;

import org.springframework.validation.annotation.Validated;

import com.jrm.model.Specialty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class ParticipantCreateDto {

    @Schema(description = "Nombre completo del participante", example = "Juan Pérez", required = true)
    private String name;

    
    @Schema(description = "Centro educativo/organización", example = "IES Andalucía")
    @NotNull(message = "El centro educativo no puede ser nulo")
    private String center;

    @Schema(description = "Puntuacion total", example = "50.0", required = true)
    @DecimalMin(value = "0.0", message = "La puntuacion no puede ser negativa")
    private float totalScore;

    @Schema(description = "ID de la especialidad", example = "1", required = true)
    private Long specialty;




}
