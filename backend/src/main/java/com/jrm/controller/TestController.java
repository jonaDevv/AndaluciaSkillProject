package com.jrm.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jrm.dto.converter.ConverterDto;
import com.jrm.dto.test.TestCreateDto;
import com.jrm.dto.test.TestResponseDto;
import com.jrm.dto.test.TestUpdateDto;

import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.error.test.TestNotFoundException;
import com.jrm.model.Specialty;
import com.jrm.model.Test;
import com.jrm.repository.TestRepository;
import com.jrm.service.ApiErrorService;
import com.jrm.service.SpecialtyService;
import com.jrm.service.TestService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;
    private final ConverterDto genericDto;
    private final ApiErrorService apiErrorService;
    private final SpecialtyService specialtyService;
    private final TestRepository testRepository;
    // 1. Crea el logger para la clase
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    // Obtener todas las participantes
    @GetMapping
    public ResponseEntity<List<TestResponseDto>> getAllTest() {
        List<Test> tests = testService.findAll();
                
        return Optional.of(tests)
                .filter(list -> !list.isEmpty())
                .map(nonEmptyList -> nonEmptyList.stream()
                    .map(t -> genericDto.genericConvert(t, TestResponseDto.class))
                    .toList())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    // Obtener una participante por ID
    @GetMapping("/{id}")
    public ResponseEntity<TestResponseDto> getTestById(@PathVariable Long id) {
        return Optional.ofNullable(testService.findById(id))
                .map(t -> genericDto.genericConvert(t, TestResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TestNotFoundException(id));
    }



    @PostMapping
    public Specialty createTest(@Valid @RequestBody TestCreateDto t) {
        
        // Loggear el DTO recibido (forzando mensaje)
        logger.debug("======================= INICIO DE LOGS =======================");
        logger.debug("DTO RECIBIDO: {}", t.toString()); // Asegúrate de que TestCreateDto tenga toString()

        // Verificar si specialtyId es nulo
        if (t.getSpecialtyId() == null) {
            logger.error("¡specialtyId ES NULO EN EL DTO!");
            throw new IllegalArgumentException("specialtyId no puede ser nulo");
        }

        Specialty specialty = specialtyService.findById(t.getSpecialtyId())
            .orElseThrow(() -> {
                logger.error("Specialty no encontrada con ID: {}", t.getSpecialtyId());
                return new SpecialtyNotFoundException(t.getSpecialtyId());
            });

        logger.debug("Specialty encontrada: {}", specialty.toString());
        logger.debug("======================= FIN DE LOGS =======================");
        
        return specialty;
    }


    // Actualizar una participante
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTest(@Valid @PathVariable Long id,
            @Valid @RequestBody TestUpdateDto testUpdateDTO) {
        
        return testService.findById(id)
                .map(t -> {
                    
                    // Actualizar campos básicos
                    t.setStatement(testUpdateDTO.getStatement());
                    t.setMaxScore(testUpdateDTO.getMaxScore());
                    

                    // Manejar specialty
                    Optional.ofNullable(testUpdateDTO.getSpecialtyId())
                            .ifPresent(specialtyId -> {
                                t.setSpecialty(specialtyService.findById(specialtyId)
                                .orElseThrow(() -> new SpecialtyNotFoundException(specialtyId)));
                            });
                    
                    // Guardar cambios y retornar el usuario actualizado
                    return testService.update(id, t); // ¡Aquí falta el return!
                })
                .map(p->genericDto.genericConvert(p, TestResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TestNotFoundException(id));
        
    }

    

    // Eliminar una participante
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTest(@PathVariable Long id) {
        
        return testService.findById(id)
                .map(t -> {
                    testService.delete(id); // Eliminar al usuario
                    return ResponseEntity.ok().build(); // Retornar una respuesta vacía con estado 200 OK
                })
                .orElseThrow(() -> new TestNotFoundException(id)); // Lanzar excepción si no se encuentra el usuario
    }
    


}
