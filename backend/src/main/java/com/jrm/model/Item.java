package com.jrm.model;



import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data  @AllArgsConstructor
@NoArgsConstructor
@Validated
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La descripcion no puede ser nula")
    private String description;

    @NotNull
    @Positive(message = "El valor debe ser positivo")
    private int weight;

    @DecimalMin(value = "0.0", message = "La valoracion no puede ser negativa")
    private float percentage;

    // Relación N:1 con Test
    @ManyToOne(fetch = FetchType.LAZY) // Carga perezosa por defecto
    @JoinColumn(name = "test_id") // Nombre de la columna en la tabla Item
    private Test test;



}
