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
import com.jrm.service.ParticipantService;
import com.jrm.service.SpecialtyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private final ConverterDto genericDto;
    private final ParticipantService participantService;
    private final SpecialtyService specialtyService;

    // Obtener todas las participantes
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

    // Obtener una participante por ID
    @GetMapping("/{id}")
    public ResponseEntity<ParticipantResponseDto> getParticipantById(@PathVariable Long id) {
        return Optional.ofNullable(participantService.findById(id))
                .map(p -> genericDto.genericConvert(p, ParticipantResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ParticipantNotFoundException(id));
    }



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
