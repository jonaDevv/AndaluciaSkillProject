package com.jrm.dto.participant;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class ParticipantUpdateDto {

  
    @NotNull(message = "El nombre del participante no puede ser nulo")
    private String name;

    @NotNull(message = "El cntro educativo no puede ser nulo")
    private String center;

    @DecimalMin(value = "0.0", message = "La puntuacion no puede ser negativa")
    private float maximumScore;


    private Long specialtyId;
}
