package com.jrm.model;



import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.DecimalMin;
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
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DecimalMin(value = "0.0", message = "La valoracion no puede ser negativa")
    private String pFinalObtenida;

    @ManyToOne
    @JoinColumn(name = "user_id") 
    private User user;

    @ManyToOne
    @JoinColumn(name = "participant_id") 
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "prueba_id") 
    private Prueba prueba;
   


    

}
