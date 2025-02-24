package com.jrm.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jrm.service.EvaluacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/estadisticas")
@RequiredArgsConstructor
public class EstadisticasController {

    private final EvaluacionService evaluacionService;

    @GetMapping("/reasignaciones")
    public ResponseEntity<Map<Long, Long>> getEstadisticasReasignacion() {
        return ResponseEntity.ok(evaluacionService.obtenerEstadisticasReasignacion());
    }
}