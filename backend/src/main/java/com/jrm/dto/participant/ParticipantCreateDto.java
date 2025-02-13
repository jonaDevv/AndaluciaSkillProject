package com.jrm.dto.participant;

import org.springframework.validation.annotation.Validated;

import com.jrm.model.Specialty;

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

     @NotNull(message = "El nombre del participante no puede ser nulo")
    
    private String name;

    @NotNull(message = "El cntro educativo no puede ser nulo")
    private String center;

    @DecimalMin(value = "0.0", message = "La puntuacion no puede ser negativa")
    private float totalScore;


    private Long specialtyId;




}
