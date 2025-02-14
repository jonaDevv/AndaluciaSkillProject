package com.jrm.dto.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import com.jrm.model.Specialty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data  @AllArgsConstructor
@NoArgsConstructor
@Validated
public class TestResponseDto {

  
    private Long id;
   
    private String statement;

    private float totalScore;

    private LocalDateTime date;

    private String specialtyName;

    private List<Item> items = new ArrayList<>();

}
