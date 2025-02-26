package com.jrm.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jrm.dto.converter.ConverterDto;
import com.jrm.dto.participant.ParticipantCreateDto;
import com.jrm.dto.participant.ParticipantResponseDto;
import com.jrm.dto.participant.ParticipantUpdateDto;
import com.jrm.error.ApiError;
import com.jrm.error.participant.ParticipantNotFoundException;
import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.model.Participant;
import com.jrm.model.Specialty;
import com.jrm.service.EvaluacionService;
import com.jrm.service.ParticipantService;
import com.jrm.service.SpecialtyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/participant")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ParticipantController {

    private final ConverterDto genericDto;
    private final ParticipantService participantService;
    private final SpecialtyService specialtyService;
    private final EvaluacionService evaluacionService;

    // Obtener todas las participantes
    @Operation(summary = "Obtener todos los participantes", 
              description = "Retorna una lista completa de participantes registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de participantes obtenida exitosamente",
                   content = @Content(schema = @Schema(implementation = ParticipantResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron participantes",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping
    public ResponseEntity<List<ParticipantResponseDto>> getAllParticipants() {
        List<Participant> participants = participantService.findAll();
                
        return Optional.of(participants)
                .filter(list -> !list.isEmpty())
                .map(nonEmptyList -> nonEmptyList.stream()
                    .map(p -> genericDto.genericConvert(p, ParticipantResponseDto.class))
                    .toList())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    // Endpoint para obtener los ganadores
    @Operation(summary = "Obtener ganadores", 
              description = "Lista de participantes con las mejores evaluaciones")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de ganadores obtenida",
                   content = @Content(schema = @Schema(implementation = ParticipantResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/ganadores")
    public ResponseEntity<?> obtenerGanadores() {
        try {
            List<ParticipantResponseDto> ganadores = evaluacionService.findGanadores();
            return ResponseEntity.ok(ganadores);
        } catch (Exception ex) {
            // Manejo de error adecuado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener una participante por ID
    @Operation(summary = "Obtener participante por ID", 
              description = "Busca un participante específico por su identificador único")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Participante encontrado",
                   content = @Content(schema = @Schema(implementation = ParticipantResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Participante no encontrado",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ParticipantResponseDto> getParticipantById(@PathVariable Long id) {
        return Optional.ofNullable(participantService.findById(id))
                .map(p -> genericDto.genericConvert(p, ParticipantResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ParticipantNotFoundException(id));
    }


    @Operation(summary = "Crear nuevo participante", 
              description = "Registra un nuevo participante en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Participante creado exitosamente",
                   content = @Content(schema = @Schema(implementation = ParticipantResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                   content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<?> createParticipant(@Valid @RequestBody ParticipantCreateDto participantCreateDTO) {
        
        Participant participant = genericDto.genericConvert(participantCreateDTO, Participant.class);

        Specialty specialty = specialtyService.findById(participantCreateDTO.getSpecialty())
                .orElseThrow(() -> new SpecialtyNotFoundException(participantCreateDTO.getSpecialty()));
        //  Convertir DTO a entidad

        // participant.setSpecialty(specialtyService.findById(participantCreateDTO.getSpecialtyId())
        // .orElseThrow(() -> new SpecialtyNotFoundException(participantCreateDTO.getSpecialtyId())));
        participant.setSpecialty(specialty);
        //  Guardar y retornar respuesta
        Participant savedParticipant = participantService.save(participant);
        ParticipantResponseDto responseDto = genericDto.genericConvert(savedParticipant, ParticipantResponseDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // Actualizar una participante
    @Operation(summary = "Actualizar participante", 
              description = "Actualiza los datos de un participante existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Participante actualizado exitosamente",
                   content = @Content(schema = @Schema(implementation = ParticipantResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                   content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Participante o especialidad no encontrada",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateParticipant(@Valid @PathVariable Long id,
            @Valid @RequestBody ParticipantUpdateDto participantUpdateDTO) {
        
        return participantService.findById(id)
                .map(participant -> {
                    // Actualizar campos básicos
                    participant.setName(participantUpdateDTO.getName());
                    participant.setCenter(participantUpdateDTO.getCenter());
                    participant.setTotalScore(participantUpdateDTO.getTotalScore());
                    
                    // Manejar specialty
                    Optional.ofNullable(participantUpdateDTO.getSpecialtyId())
                            .ifPresent(specialtyId -> {
                                participant.setSpecialty(specialtyService.findById(specialtyId)
                                .orElseThrow(() -> new SpecialtyNotFoundException(specialtyId)));
                            });
                    
                    // Guardar cambios y retornar el usuario actualizado
                    return participantService.update(id, participant); // ¡Aquí falta el return!
                })
                .map(p->genericDto.genericConvert(p, ParticipantResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ParticipantNotFoundException(id));
        
    }

    

    // Eliminar una participante
    @Operation(summary = "Eliminar participante", 
              description = "Elimina permanentemente un participante del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Participante eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Participante no encontrado",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParticipant(@PathVariable Long id) {
        
        return participantService.findById(id)
                .map(p -> {
                    participantService.delete(id); // Eliminar al usuario
                    return ResponseEntity.ok().build(); // Retornar una respuesta vacía con estado 200 OK
                })
                .orElseThrow(() -> new ParticipantNotFoundException(id)); // Lanzar excepción si no se encuentra el usuario
    }



}
