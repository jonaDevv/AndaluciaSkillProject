package com.jrm.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jrm.error.ApiError;
import com.jrm.service.EvaluacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "EstadisticasController", description = "Controlador de estadisticas")
@RestController
@RequestMapping("/estadisticas")
@RequiredArgsConstructor
public class EstadisticasController {

    private final EvaluacionService evaluacionService;

    
    @Operation(
        summary = "Estadisticas de registro",
        description = "Las estadisticas de registro de reasignacion",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Devuelve el resultado de la operación",
                content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(
                responseCode = "401",
                description = "No encontradas",
                content = @Content(schema = @Schema(implementation = ApiError.class)))
        }
    )
    @GetMapping("/reasignaciones")
    public ResponseEntity<Map<Long, Long>> getEstadisticasReasignacion() {

        
        return ResponseEntity.ok(evaluacionService.obtenerEstadisticasReasignacion());
    }
}