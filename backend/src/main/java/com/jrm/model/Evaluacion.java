package com.jrm.model;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
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
    private float pFinalObtenida;

    @DecimalMin(value = "0.0", message = "La valoracion no puede ser negativa")
    private float porcentajeFinalObtenido;

   @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'PENDIENTE'")
    private String estado = "PENDIENTE"; // Valor por defecto
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") 
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable=true) 
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prueba_id", nullable=true) 
    private Prueba prueba;
    

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvaluacionItem> items = new ArrayList<>();

    @Version
    private Long version;
    
    @ElementCollection
    @CollectionTable(name = "reasignaciones", joinColumns = @JoinColumn(name = "evaluacion_id"))
    private List<Reasignacion> historialReasignaciones = new ArrayList<>();
}


   


    


