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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/specialty")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Gestión de Especialidades", description = "Operaciones CRUD para especialidades técnicas")
public class SpecialtyController {

    private final SpecialtyService specialtyService;
    private final ConverterDto genericDto;
    private final ApiErrorService apiErrorService;

    // Obtener todas las especialidades
    @Operation(summary = "Obtener todas las especialidades", 
               description = "Retorna una lista completa de especialidades registradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de especialidades encontrada",
                   content = @Content(schema = @Schema(implementation = SpecialtyResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron especialidades",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
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
    @Operation(summary = "Obtener especialidad por ID", 
               description = "Busca una especialidad específica por su identificador único")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Especialidad encontrada",
                   content = @Content(schema = @Schema(implementation = SpecialtyResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyResponseDTO> getSpecialtyById(@PathVariable Long id) {
        return Optional.ofNullable(specialtyService.findById(id))
                .map(s -> genericDto.genericConvert(s, SpecialtyResponseDTO.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new SpecialtyNotFoundException(id));
    }

    // Crear una nueva especialidad
    @Operation(summary = "Crear nueva especialidad", 
               description = "Registra una nueva especialidad técnica en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Especialidad creada exitosamente",
                   content = @Content(schema = @Schema(implementation = SpecialtyResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                   content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "409", description = "Conflicto de datos únicos",
                   content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
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

        try{
            Specialty savedSpecialty = specialtyService.save(specialty);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(genericDto.genericConvert(savedSpecialty, SpecialtyResponseDTO.class));

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        
    }

   
    @Operation(summary = "Actualizar especialidad", 
               description = "Actualiza los datos de una especialidad existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Especialidad actualizada exitosamente",
                   content = @Content(schema = @Schema(implementation = SpecialtyResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                   content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada",
                   content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "409", description = "Conflicto de datos únicos",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSpecialty(
            @PathVariable Long id,
            @Valid @RequestBody SpecialtyUpdateDTO specialtyUpdateDTO) {

        Specialty s= specialtyService.findById(id).orElseThrow(() -> new SpecialtyNotFoundException(id));
        
        if(specialtyService.findByCod(specialtyUpdateDTO.getCod()).isPresent() && !specialtyUpdateDTO.getCod().equals(s.getCod())){
            ApiError apiError = apiErrorService.getErrorMessage("El código de la especialidad " + specialtyUpdateDTO.getCod() + " ya existe");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        }

        if(specialtyService.findByName(specialtyUpdateDTO.getName()).isPresent() && !specialtyUpdateDTO.getName().equals(s.getName())){
            ApiError apiError = apiErrorService.getErrorMessage("El nombre de la especialidad " + specialtyUpdateDTO.getName() + " ya existe");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        }


        Specialty specialtyUpdate = genericDto.genericConvert(specialtyUpdateDTO, Specialty.class);

        // Intentamos actualizar la especialidad usando un Optional
        return Optional.ofNullable(specialtyService.update(id, specialtyUpdate))
                .filter(updatedSpecialty -> updatedSpecialty != null)  // Verificamos que la especialidad no sea null
                .map(updatedSpecialty -> genericDto.genericConvert(updatedSpecialty, SpecialtyResponseDTO.class))  // Convertimos a DTO
                .map(ResponseEntity::ok)  // Si se encuentra y se actualiza, devolvemos un 200 OK
                .orElseThrow(() -> new SpecialtyNotFoundException(id)); // Si no se encuentra, devolvemos un 404 Not Found
    }

    // Eliminar una especialidad
    @Operation(summary = "Eliminar especialidad", 
               description = "Elimina permanentemente una especialidad del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Especialidad eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada",
                   content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
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