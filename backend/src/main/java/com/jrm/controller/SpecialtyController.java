package com.jrm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jrm.dto.converter.ConverterDto;

import com.jrm.dto.specialty.SpecialtyCreateDto;
import com.jrm.dto.specialty.SpecialtyResponseDTO;
import com.jrm.dto.specialty.SpecialtyUpdateDTO;
import com.jrm.error.ApiError;
import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.model.Specialty;
import com.jrm.service.ApiErrorService;
import com.jrm.service.SpecialtyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/specialty")
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyService specialtyService;
    private final ConverterDto genericDto;
    private final ApiErrorService apiErrorService;

    // Obtener todas las especialidades
    @GetMapping
    public ResponseEntity<List<SpecialtyResponseDTO>> getAllSpecialties() {
        List<SpecialtyResponseDTO> specialties = specialtyService.findAll().stream()
                .map(s -> genericDto.genericConvert(s, SpecialtyResponseDTO.class))
                .toList();
        
        return specialties.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(specialties);
    }

    // Obtener una especialidad por ID
    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyResponseDTO> getSpecialtyById(@PathVariable Long id) {
        return Optional.ofNullable(specialtyService.findById(id))
                .map(s -> genericDto.genericConvert(s, SpecialtyResponseDTO.class))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva especialidad
    @PostMapping
    public ResponseEntity<?> createSpecialty(
            @Valid @RequestBody SpecialtyCreateDto specialtyCreateDTO) {

        if(specialtyService.findByCod(specialtyCreateDTO.getCod()).isPresent()){
            ApiError apiError = apiErrorService.getErrorMessage("El código de la especialidad " + specialtyCreateDTO.getCod() + " ya existe");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        }
        
        Specialty specialty = genericDto.genericConvert(specialtyCreateDTO, Specialty.class);
        Specialty savedSpecialty = specialtyService.save(specialty);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(genericDto.genericConvert(savedSpecialty, SpecialtyResponseDTO.class));
    }

    // Actualizar una especialidad
    @PutMapping("/{id}")
    public ResponseEntity<SpecialtyResponseDTO> updateSpecialty(
            @PathVariable Long id,
            @Valid @RequestBody SpecialtyUpdateDTO specialtyUpdateDTO) {
        
        Specialty specialtyUpdate = genericDto.genericConvert(specialtyUpdateDTO, Specialty.class);
        Specialty updatedSpecialty = specialtyService.update(id, specialtyUpdate);
        
        return ResponseEntity.ok(
            genericDto.genericConvert(updatedSpecialty, SpecialtyResponseDTO.class)
        );
    }

    // Eliminar una especialidad
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
        
        try {
            specialtyService.delete(id);
            return ResponseEntity.ok().build();

        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}