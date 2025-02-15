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
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    @PostMapping
    public ResponseEntity<?> createParticipant(@Valid @RequestBody ParticipantCreateDto participantCreateDTO) {

        

        //  Convertir DTO a entidad
        Participant participant = genericDto.genericConvert(participantCreateDTO, Participant.class);

        
        participant.setSpecialty(specialtyService.findById(participantCreateDTO.getSpecialtyId())
        .orElseThrow(() -> new SpecialtyNotFoundException(participantCreateDTO.getSpecialtyId())));

     

        //  Guardar y retornar respuesta
        Participant savedParticipant = participantService.save(participant);
        ParticipantResponseDto responseDto = genericDto.genericConvert(savedParticipant, ParticipantResponseDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // Actualizar una participante
    @PutMapping("/{id}")
    public ResponseEntity<?> updateParticipant(@Valid @PathVariable Long id,
            @Valid @RequestBody ParticipantUpdateDto participantUpdateDTO) {
        
        Participant participant = participantService.findById(id).orElseThrow(() -> new ParticipantNotFoundException(id));

        participant.setName(participantUpdateDTO.getName());
        participant.setCenter(participantUpdateDTO.getCenter());
        participant.setTotalScore(participantUpdateDTO.getTotalScore());
        
         // Mapear specialtyId a un objeto Specialty=
         if (participantUpdateDTO.getSpecialtyId() != null) {
            Specialty specialty = new Specialty();
            specialty.setId(participantUpdateDTO.getSpecialtyId());
            participant.setSpecialty(specialty);
        }
        
        return ResponseEntity.ok(participantService.update(id, participant));
        
    }

    

    // Eliminar una participante
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        try {
            
            participantService.delete(id);
            return ResponseEntity.ok().build();

        } catch (ParticipantNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



}
