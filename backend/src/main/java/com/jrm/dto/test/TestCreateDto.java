package com.jrm.dto.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import com.jrm.model.Specialty;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class TestCreateDto {

    @NotNull(message = "El enunciado del especialidad no puede ser nulo")
    private String statement;

    @DecimalMin(value = "0.0", message = "La puntuacion no puede ser negativa")
    private float totalScore;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Future(message = "La fecha debe de ser en el futuro")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "specialty_id") 
    private Long specialtyId;

     private List<Item> items = new ArrayList<>();





}
