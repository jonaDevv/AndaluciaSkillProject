package com.jrm.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
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
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El enunciado del especialidad no puede ser nulo")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s.,!?-]+$", 
    message = "El enunciado solo puede contener letras, números y espacios")
    private String statement;

    @DecimalMin(value = "0.0", message = "La puntuacion no puede ser negativa")
    private float maxScore;


    @ManyToOne
    @JoinColumn(name = "specialty_id") 
    private Specialty specialty;


    // Relación 1:N con Item
    @OneToMany(
        mappedBy = "test", // Nombre del campo en la entidad Item que mapea esta relación
        cascade = CascadeType.ALL, // Propaga operaciones (save, delete) a los Items
        orphanRemoval = true // Elimina Items huérfanos (sin referencia a Test)
    )
    private List<Item> items = new ArrayList<>();




}
