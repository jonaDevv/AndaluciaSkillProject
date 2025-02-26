package com.jrm.dto.evaluacion;

import java.util.List;

import com.jrm.dto.evaluacionItem.EvaluacionItemDTO;
import com.jrm.dto.participant.ParticipantResponseDto;
import com.jrm.dto.prueba.PruebaResponseDto;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class EvaluacionDetailsDTO {

    private Long id;
    private Float pFinalObtenida;
    private Float porcentajeFinalObtenido;
    private String estado;
    private Long pruebaId;
    private Long participantId;
    private List<EvaluacionItemDTO> items;
}
