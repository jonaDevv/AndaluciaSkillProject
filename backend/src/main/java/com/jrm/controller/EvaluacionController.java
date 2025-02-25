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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/evaluacion")
// @RequiredArgsConstructor
// public class EvaluacionController {

//     private final EvaluacionService evaluacionService;
//     private final ConverterDto converter;


//     @GetMapping
//     public ResponseEntity<?> getAllEvaluacions() {
        
//         List<EvaluacionResponseDto> evaluacions = 
//                 evaluacionService.findAll()
//                 .stream()
//                 .map(e -> converter.genericConvert(e, EvaluacionResponseDto.class))
//                 .toList();
        
//         return Optional.of(evaluacions)
//                 .filter(list -> !list.isEmpty())
//                 .map(nonEmptyList -> nonEmptyList.stream()
//                     .map(e -> converter.genericConvert(e, EvaluacionResponseDto.class))
//                     .toList())
//                 .map(ResponseEntity::ok)                
//                 .orElseGet(() -> ResponseEntity.notFound().build());

//     }


//     @GetMapping("/{id}")
//     public ResponseEntity<?> getEvaluacionById(@PathVariable Long id) {
        
//         return Optional.ofNullable(evaluacionService.findById(id))
//                 .map(e -> converter.genericConvert(e, EvaluacionResponseDto.class))
//                 .map(ResponseEntity::ok)
//                 .orElseThrow(() -> new EvaluacionNotFoundException(id));
//     }


//     @PostMapping
//     public ResponseEntity<?> createEvaluacion(@RequestBody EvaluacionCreateDto dto) {
        
//         Evaluacion evaluacion = converter.genericConvert(dto, Evaluacion.class);

//         evaluacion.setUser(converter.genericConvert(dto.getUser(), User.class));
//         evaluacion.setParticipant(converter.genericConvert(dto.getParticipant(), Participant.class));
//         evaluacion.setPrueba(converter.genericConvert(dto.getPrueba(), Prueba.class));

//         Evaluacion savedEvaluacion = evaluacionService.save(evaluacion);

//         EvaluacionResponseDto responseDto = converter.genericConvert(savedEvaluacion, EvaluacionResponseDto.class);

//         return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
//     }


//     // @PutMapping("/{id}")
//     // public ResponseEntity<?> updateEvaluacion(@PathVariable Long id, @Valid @RequestBody EvaluacionUpdateDto dto) {
        
//     //     return evaluacionService.findById(id)
//     //             .map(evaluacion -> {
                    
//     //                 evaluacion.setPFinalObtenida(dto.getPFinalObtenida());
//     //                 return evaluacionService.update(id, evaluacion); // ¡Aquí falta el return!
//     //             })
//     //             .map(p->converter.genericConvert(p, EvaluacionResponseDto.class))
//     //             .map(ResponseEntity::ok)
//     //             .orElseThrow(() -> new EvaluacionNotFoundException(id));
        
//     // }

//     @PutMapping("/{id}/reasignar")
//     public ResponseEntity<Evaluacion> reasignarEvaluacion(@PathVariable Long id) {
//         try {
//             Evaluacion evaluacionReasignada = evaluacionService.reasignarEvaluacion(id);
//             return ResponseEntity.ok(evaluacionReasignada);
//         } catch (NoExpertsAvailableException e) {
//             return ResponseEntity.status(HttpStatus.CONFLICT).build();
//         } catch (EvaluacionNotFoundException e) {
//             return ResponseEntity.notFound().build();
//         }
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<?> deleteEvaluacion(@PathVariable Long id) {
        
//         return evaluacionService.findById(id)
//                 .map(p -> {
//                     evaluacionService.delete(id); // Eliminar al usuario
//                     return ResponseEntity.ok().build(); // Retornar una respuesta vacía con estado 200 OK
//                 })
//                 .orElseThrow(() -> new EvaluacionNotFoundException(id)); // Lanzar excepción si no se encuentra el usuario
//     }

    



// }

@RestController
@RequestMapping("/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {

    private final EvaluacionService evaluacionService;
    private final EvaluacionItemService evItemService;

    @GetMapping("/pendientes")
    public ResponseEntity<List<EvaluacionDTO>> getEvaluacionesPendientes(
        @AuthenticationPrincipal UserDetails userDetails) {
        
        List<Evaluacion> evaluaciones = evaluacionService.findPendientesByUser(userDetails.getUsername());
        List<EvaluacionDTO> dtos = evaluaciones.stream()
            .map(evaluacionService::convertToDTO)
            .toList();
        
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/finalizada")
    public ResponseEntity<List<EvaluacionDTO>> getEvaluacionesFinalizadas(
        @AuthenticationPrincipal UserDetails userDetails) {
        
        List<Evaluacion> evaluaciones = evaluacionService.findFinalizadasByUser(userDetails.getUsername());
        List<EvaluacionDTO> dtos = evaluaciones.stream()
            .map(evaluacionService::convertToDTO)
            .toList();
        
        return ResponseEntity.ok(dtos);
    }


    // Endpoint para obtener los ganadores
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

    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionDetailsDTO> getEvaluacionDetails(@PathVariable Long id) {
        return ResponseEntity.ok(evaluacionService.getEvaluacionDetails(id));
    }

    @PostMapping("/{id}/calcular")
    public ResponseEntity<EvaluacionDetailsDTO> calcularEvaluacion(@PathVariable Long id) {
        return ResponseEntity.ok(evaluacionService.calcularResultados(id));
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<EvaluacionDetailsDTO> finalizarEvaluacion(@PathVariable Long id) {
        return ResponseEntity.ok(evaluacionService.finalizarEvaluacion(id));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<EvaluacionItemDTO> updateEvaluacionItem(
        @PathVariable Long itemId,
        @RequestBody EvaluacionItemDTO dto) {
        
        EvaluacionItem updated = evItemService.updateEvaluacionItem(itemId, dto);
        return ResponseEntity.ok(evaluacionService.mapToDTO(updated));
    }
}