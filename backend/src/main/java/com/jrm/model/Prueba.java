package com.jrm.model;


import java.util.ArrayList;
import java.util.List;



import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Entity
public class Prueba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El enunciado del especialidad no puede ser nulo")
    // @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s.,!?-]+$", 
    // message = "El enunciado solo puede contener letras, números y espacios")
    private String enunciado;

    @DecimalMin(value = "0.0", message = "La puntuacion no puede ser negativa")
    private float maxScore;


    @ManyToOne
    @JoinColumn(name = "specialty_id", nullable = true)
    private Specialty specialty;

    // Campo para la URL o ruta del PDF que se persistirá
    private String pdfUrl;
    
    // Campo para capturar el archivo PDF; no se persistirá
    @Transient
    private org.springframework.web.multipart.MultipartFile pdfFile;

    // Relación 1:N con Item
    @OneToMany(
        mappedBy = "prueba", // Nombre del campo en la entidad Item que mapea esta relación
        cascade = CascadeType.ALL, // Propaga operaciones (save, delete) a los Items
        orphanRemoval = true // Elimina Items huérfanos (sin referencia a Test)
    )
    @JsonManagedReference
    private List<Item> items = new ArrayList<>();




}
