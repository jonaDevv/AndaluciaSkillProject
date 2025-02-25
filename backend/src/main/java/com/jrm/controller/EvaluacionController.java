package com.jrm.controller;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jrm.dto.converter.ConverterDto;
import com.jrm.dto.evaluacion.EvaluacionCreateDto;
import com.jrm.dto.evaluacion.EvaluacionDTO;
import com.jrm.dto.evaluacion.EvaluacionDetailsDTO;
import com.jrm.dto.evaluacion.EvaluacionResponseDto;
import com.jrm.dto.evaluacion.EvaluacionUpdateDto;
import com.jrm.dto.evaluacionItem.EvaluacionItemDTO;
import com.jrm.dto.participant.ParticipantResponseDto;
import com.jrm.error.ApiError;
import com.jrm.error.evaluacion.EvaluacionNotFoundException;
import com.jrm.error.user.NoExpertsAvailableException;
import com.jrm.model.Evaluacion;
import com.jrm.model.EvaluacionItem;
import com.jrm.model.Participant;
import com.jrm.model.Prueba;
import com.jrm.model.User;
import com.jrm.service.EvaluacionItemService;
import com.jrm.service.EvaluacionService;
import com.jrm.service.ParticipantService;
import com.jrm.service.PruebaService;
import com.jrm.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/evaluaciones")
@RequiredArgsConstructor
@Tag(name = "Evaluaciones", description = "Gestión de evaluaciones de competencias")
@SecurityRequirement(name = "bearerAuth")
public class EvaluacionController {

    private final EvaluacionService evaluacionService;
    private final EvaluacionItemService evItemService;

    @Operation(summary = "Obtener evaluaciones pendientes", 
               description = "Lista de evaluaciones pendientes para el usuario autenticado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de evaluaciones obtenida"),
        @ApiResponse(responseCode = "401", description = "No autorizado", 
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Acceso prohibido",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/pendientes")
    public ResponseEntity<List<EvaluacionDTO>> getEvaluacionesPendientes(
        @AuthenticationPrincipal UserDetails userDetails) {
        
        List<Evaluacion> evaluaciones = evaluacionService.findPendientesByUser(userDetails.getUsername());
        List<EvaluacionDTO> dtos = evaluaciones.stream()
            .map(evaluacionService::convertToDTO)
            .toList();
        
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener evaluaciones finalizadas", 
               description = "Lista de evaluaciones finalizadas por el usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de evaluaciones obtenida"),
        @ApiResponse(responseCode = "401", description = "No autorizado",
                   content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Acceso prohibido",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/finalizada")
    public ResponseEntity<List<EvaluacionDTO>> getEvaluacionesFinalizadas(
        @AuthenticationPrincipal UserDetails userDetails) {
        
        List<Evaluacion> evaluaciones = evaluacionService.findFinalizadasByUser(userDetails.getUsername());
        List<EvaluacionDTO> dtos = evaluaciones.stream()
            .map(evaluacionService::convertToDTO)
            .toList();
        
        return ResponseEntity.ok(dtos);
    }


    @Operation(summary = "Obtener detalles de evaluación", 
               description = "Detalles completos de una evaluación específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalles de la evaluación",
                   content = @Content(schema = @Schema(implementation = EvaluacionDetailsDTO.class))),
        @ApiResponse(responseCode = "404", description = "Evaluación no encontrada",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionDetailsDTO> getEvaluacionDetails(@PathVariable Long id) {
        return ResponseEntity.ok(evaluacionService.getEvaluacionDetails(id));
    }


    @Operation(summary = "Calcular resultados", 
               description = "Calcular los resultados de una evaluación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultados calculados",
                   content = @Content(schema = @Schema(implementation = EvaluacionDetailsDTO.class))),
        @ApiResponse(responseCode = "400", description = "Evaluación no válida para cálculo",
                   content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Evaluación no encontrada",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping("/{id}/calcular")
    public ResponseEntity<EvaluacionDetailsDTO> calcularEvaluacion(@PathVariable Long id) {
        return ResponseEntity.ok(evaluacionService.calcularResultados(id));
    }



    @Operation(summary = "Finalizar evaluación", 
               description = "Marcar una evaluación como finalizada")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluación finalizada",
                   content = @Content(schema = @Schema(implementation = EvaluacionDetailsDTO.class))),
        @ApiResponse(responseCode = "400", description = "No se puede finalizar la evaluación",
                   content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Evaluación no encontrada",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping("/{id}/finalizar")
    public ResponseEntity<EvaluacionDetailsDTO> finalizarEvaluacion(@PathVariable Long id) {
        return ResponseEntity.ok(evaluacionService.finalizarEvaluacion(id));
    }

    @Operation(summary = "Actualizar ítem de evaluación", 
               description = "Actualizar la valoración y justificación de un ítem específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ítem actualizado",
                   content = @Content(schema = @Schema(implementation = EvaluacionItemDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                   content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Ítem no encontrado",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/items/{itemId}")
    public ResponseEntity<EvaluacionItemDTO> updateEvaluacionItem(
        @PathVariable Long itemId,
        @RequestBody EvaluacionItemDTO dto) {
        
        EvaluacionItem updated = evItemService.updateEvaluacionItem(itemId, dto);
        return ResponseEntity.ok(evaluacionService.mapToDTO(updated));
    }
}