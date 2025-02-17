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
import com.jrm.dto.converter.TestConverterDto;
import com.jrm.dto.participant.ParticipantResponseDto;
import com.jrm.dto.prueba.PruebaCreateDto;
import com.jrm.dto.prueba.PruebaResponseDto;
import com.jrm.dto.prueba.PruebaUpdateDto;
import com.jrm.error.prueba.PruebaNotFoundException;
import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.model.Participant;
import com.jrm.model.Specialty;
import com.jrm.model.Prueba;
import com.jrm.repository.PruebaRepository;
import com.jrm.service.ApiErrorService;
import com.jrm.service.SpecialtyService;
import com.jrm.service.PruebaService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/prueba")
@RequiredArgsConstructor
public class PruebaController {

    private final PruebaService pruebaService;
    private final ConverterDto genericDto;
    private final SpecialtyService specialtyService;


    // Obtener todas las participantes
    @GetMapping
    public ResponseEntity<List<PruebaResponseDto>> getAllTest() {
        List<Prueba> tests = pruebaService.findAll();
                
        return Optional.of(tests)
                .filter(list -> !list.isEmpty())
                .map(nonEmptyList -> nonEmptyList.stream()
                    .map(t -> genericDto.genericConvert(t, PruebaResponseDto.class))
                    .toList())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    // Obtener una participante por ID
    @GetMapping("/{id}")
    public ResponseEntity<PruebaResponseDto> getTestById(@PathVariable Long id) {
        return Optional.ofNullable(pruebaService.findById(id))
                .map(t -> genericDto.genericConvert(t, PruebaResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PruebaNotFoundException(id));
    }



    @PostMapping
    public ResponseEntity<?> createPrueba( @RequestBody PruebaCreateDto t) {
        
        System.out.println("Creando prueba"+t);

        Specialty specialty = specialtyService.findById(t.getSpecialtyId())
                .orElseThrow(() -> new SpecialtyNotFoundException(t.getSpecialtyId()));
          //  Convertir DTO a entidad
        Prueba prueba = Prueba.builder()
                .enunciado(t.getEnunciado())
                .maxScore(t.getMaxScore())
                .specialty(specialty)
                .build();
        

        System.out.println(prueba);

        prueba.setSpecialty(specialty);

        //  Guardar y retornar respuesta
        Prueba savedPrueba = pruebaService.save(prueba);
        PruebaResponseDto responseDto = genericDto.genericConvert(savedPrueba, PruebaResponseDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // @PostMapping
    // public ResponseEntity<?> createTest(@RequestBody TestCreateDto t) {
    //     System.out.println("Solicitud recibida desde Postman: " + t); // Imprime el DTO

    //     try {
            
    //         Specialty specialty = specialtyService.findById(1L)
    //                     .orElseThrow(() -> new SpecialtyNotFoundException(t.getSpecialtyId()));
    //               //  Convertir DTO a entidad
    //             Test test = Test.builder()
    //                     .statement(t.getStatement())
    //                     .maxScore(t.getMaxScore())
    //                     .specialty(specialty)
    //                     .build();
    //     } catch (Exception e) {
    //         System.err.println("Error al crear el Test: " + e.getMessage()); // Imprime el error
    //         // ...
    //     }
    //     return ResponseEntity.badRequest().body(t);
    // }

    // @PostMapping
    // public ResponseEntity<?> createTest(@Valid @RequestBody Test test) {
    //     // // Asegúrate de que test no sea null
    //     // if (test == null || test.getStatement() == null) {
    //     //     throw new IllegalArgumentException("El enunciado no puede ser nulo");
    //     // }
    
    //     // Configurar Specialty si no viene en el objeto
    //     if (test.getSpecialty() == null || test.getSpecialty().getId() == null) {
    //         throw new SpecialtyNotFoundException(test.getSpecialty().getId());
    //     }
    
    //     Specialty specialty = specialtyService.findById(test.getSpecialty().getId())
    //             .orElseThrow(() -> new SpecialtyNotFoundException(test.getSpecialty().getId()));
    //     test.setSpecialty(specialty);
    
    //     // Guardar y retornar respuesta
    //     Test savedTest = testService.save(test);
    //     TestResponseDto responseDto = genericDto.genericConvert(savedTest, TestResponseDto.class);
    
    //     return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    // }


    // Actualizar una participante
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrueba(@Valid @PathVariable Long id,
            @Valid @RequestBody PruebaUpdateDto pruebaUpdateDTO) {
        
        return pruebaService.findById(id)
                .map(t -> {
                    
                    // Actualizar campos básicos
                    t.setEnunciado(pruebaUpdateDTO.getEnunciado());
                    t.setMaxScore(pruebaUpdateDTO.getMaxScore());
                    

                    // Manejar specialty
                    Optional.ofNullable(pruebaUpdateDTO.getSpecialtyId())
                            .ifPresent(specialtyId -> {
                                t.setSpecialty(specialtyService.findById(specialtyId)
                                .orElseThrow(() -> new SpecialtyNotFoundException(specialtyId)));
                            });
                    
                    // Guardar cambios y retornar el usuario actualizado
                    return pruebaService.update(id, t); // ¡Aquí falta el return!
                })
                .map(p->genericDto.genericConvert(p, PruebaResponseDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PruebaNotFoundException(id));
        
    }

    

    // Eliminar una participante
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrueba(@PathVariable Long id) {
        
        return pruebaService.findById(id)
                .map(p -> {
                    pruebaService.delete(id); // Eliminar al usuario
                    return ResponseEntity.ok().build(); // Retornar una respuesta vacía con estado 200 OK
                })
                .orElseThrow(() -> new PruebaNotFoundException(id)); // Lanzar excepción si no se encuentra el usuario
    }
    


}
