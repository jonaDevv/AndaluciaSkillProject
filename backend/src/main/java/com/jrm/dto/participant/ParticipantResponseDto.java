package com.jrm.dto.participant;

import com.jrm.model.Specialty;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class ParticipantResponseDto {

    
    private Long id;
    private String name;
    private String center;
    private float maximumScore;
    private String specialtyName;




}
