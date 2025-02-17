package com.jrm.dto.prueba;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Validated
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class PruebaCreateDto {

   
    @NotNull(message = "El enunciado no puede ser nulo")
    private String enunciado;

    @DecimalMin(value = "0.0", message = "La puntuacion no puede ser negativa")
    private float maxScore;

   @JsonProperty("specialtyId") 
    private Long specialtyId;

    @Builder.Default
    private List<Item> items = new ArrayList<>();





}
