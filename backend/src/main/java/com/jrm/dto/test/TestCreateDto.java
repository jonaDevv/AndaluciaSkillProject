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
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class TestCreateDto {

   
    private String statement;

    @DecimalMin(value = "0.0", message = "La puntuacion no puede ser negativa")
    private float maxScore;

    @NotNull(message = "EL ID de la especialidad no puede ser nulo")
    private Long specialtyId;

    @Builder.Default
    private List<Item> items = new ArrayList<>();





}
