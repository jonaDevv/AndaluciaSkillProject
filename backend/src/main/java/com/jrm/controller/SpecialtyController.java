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
       
        List<Specialty> specialties = specialtyService.findAll();

        return Optional.of(specialties) 
                .filter(list -> !list.isEmpty())
                .map(nomEmtyList -> nomEmtyList.stream()   
                .map(s -> genericDto.genericConvert(s, SpecialtyResponseDTO.class))
                .toList())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener una especialidad por ID
    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyResponseDTO> getSpecialtyById(@PathVariable Long id) {
        return Optional.ofNullable(specialtyService.findById(id))
                .map(s -> genericDto.genericConvert(s, SpecialtyResponseDTO.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new SpecialtyNotFoundException(id));
    }

    // Crear una nueva especialidad
    @PostMapping
    public ResponseEntity<?> createSpecialty(
            @Valid @RequestBody SpecialtyCreateDto specialtyCreateDTO) {

        if(specialtyService.findByCod(specialtyCreateDTO.getCod()).isPresent()){
            ApiError apiError = apiErrorService.getErrorMessage("El código de la especialidad " + specialtyCreateDTO.getCod() + " ya existe");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        }

        if(specialtyService.findByName(specialtyCreateDTO.getName()).isPresent()){
            ApiError apiError = apiErrorService.getErrorMessage("El nombre de la especialidad " + specialtyCreateDTO.getName() + " ya existe");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        }
        
        Specialty specialty = genericDto.genericConvert(specialtyCreateDTO, Specialty.class);
        Specialty savedSpecialty = specialtyService.save(specialty);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(genericDto.genericConvert(savedSpecialty, SpecialtyResponseDTO.class));
    }

   

    @PutMapping("/{id}")
    public ResponseEntity<SpecialtyResponseDTO> updateSpecialty(
            @PathVariable Long id,
            @Valid @RequestBody SpecialtyUpdateDTO specialtyUpdateDTO) {

        Specialty specialtyUpdate = genericDto.genericConvert(specialtyUpdateDTO, Specialty.class);

        // Intentamos actualizar la especialidad usando un Optional
        return Optional.ofNullable(specialtyService.update(id, specialtyUpdate))
                .filter(updatedSpecialty -> updatedSpecialty != null)  // Verificamos que la especialidad no sea null
                .map(updatedSpecialty -> genericDto.genericConvert(updatedSpecialty, SpecialtyResponseDTO.class))  // Convertimos a DTO
                .map(ResponseEntity::ok)  // Si se encuentra y se actualiza, devolvemos un 200 OK
                .orElseThrow(() -> new SpecialtyNotFoundException(id)); // Si no se encuentra, devolvemos un 404 Not Found
    }

    // Eliminar una especialidad
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpecialty(@PathVariable Long id) {
        
        return specialtyService.findById(id)
                .map(s -> {
                    specialtyService.delete(id); // Eliminar al usuario
                    return ResponseEntity.ok().build(); // Retornar una respuesta vacía con estado 200 OK
                })
                .orElseThrow(() -> new SpecialtyNotFoundException(id)); // Lanzar excepción si no se encuentra el usuario
    }
}