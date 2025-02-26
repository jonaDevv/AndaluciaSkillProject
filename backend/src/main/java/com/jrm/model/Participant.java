package com.jrm.model;

import java.util.Optional;

import org.springframework.boot.autoconfigure.rsocket.RSocketProperties.Server.Spec;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@Validated
@NoArgsConstructor
public class Participant {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre del participante no puede ser nulo")
    private String name;

    @NotNull(message = "El cntro educativo no puede ser nulo")
    private String center;

    @DecimalMin(value = "0.0", message = "La puntuacion no puede ser negativa")
    private float totalScore;


    @ManyToOne
    @JoinColumn(name = "specialty_id", nullable=true) 
    private Specialty specialty;



}
