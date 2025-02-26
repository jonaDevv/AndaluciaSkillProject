package com.jrm.model;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class EvaluacionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The code snippet provided is defining a Java entity class named `EvaluacionItem` that represents
    // an evaluation item. Here is an explanation of the annotations used:
    @DecimalMin(value = "0.0", message = "La valoracion no puede ser negativa")
    @DecimalMax(value = "100.0", message = "La valoracion no puede ser mayor que 100")
    private float valoracion;

    
    private String description;
   
    
    private String justificacion;

    @ManyToOne
    @JoinColumn(name = "evaluacion_id") 
    private Evaluacion evaluacion;

    @ManyToOne
    @JoinColumn(name = "item_id") 
    private Item item;

    

   
}
