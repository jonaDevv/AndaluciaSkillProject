package com.jrm.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jrm.dto.evaluacion.EvaluacionDTO;
import com.jrm.dto.evaluacion.EvaluacionDetailsDTO;
import com.jrm.dto.evaluacionItem.EvaluacionItemDTO;
import com.jrm.error.evaluacion.EvaluacionNotFoundException;
import com.jrm.error.user.NoExpertsAvailableException;
import com.jrm.error.user.UserNotFoundException;
import com.jrm.model.Evaluacion;
import com.jrm.model.EvaluacionItem;
import com.jrm.model.Item;
import com.jrm.model.Participant;
import com.jrm.model.Prueba;
import com.jrm.model.Specialty;
import com.jrm.model.User;
import com.jrm.model.UserRole;
import com.jrm.repository.EvaluacionRepository;
import com.jrm.repository.UserRepository;
import com.jrm.repository.projection.ReasignacionStats;
import com.jrm.service.base.BaseService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluacionService implements BaseService<Evaluacion, Long> {
    
    private final EvaluacionRepository evaR;
    private final UserRepository userRepository;
   
  
    @Override
    public List<Evaluacion> findAll() {
       
        return evaR.findAll();
    }

    @Override
    public Optional<Evaluacion> findById(Long id) {
        
        return evaR.findById(id);
    }

    @Override
    public Evaluacion save(Evaluacion user) {
        
        return evaR.save(user);
    }

    @Override
    public Evaluacion update(Long id, Evaluacion t) {
       
        return evaR.save(t);
    }

    @Override
    public void delete(Long id) {
        
        Evaluacion eva = evaR.findById(id)
                                    .orElseThrow(() -> new EvaluacionNotFoundException(id));
        evaR.delete(eva);
    }

    public Evaluacion crearEvaluacion(Participant participant, Prueba prueba) {
        Specialty specialty = prueba.getSpecialty();
        List<User> experts = userRepository.findExpertsBySpecialtyOrderByEvaluationCount(
            specialty.getId(), 
            UserRole.EXPERT
        );
    
        if (experts.isEmpty()) {
            throw new UserNotFoundException(specialty.getId());
        }
    
        User expertoAsignado = experts.get(0);
    
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setUser(expertoAsignado);
        evaluacion.setParticipant(participant);
        evaluacion.setPrueba(prueba);
        evaluacion.setPFinalObtenida(0.0f);
        evaluacion.setPorcentajeFinalObtenido(0.0f);
        evaluacion.setEstado("PENDIENTE"); // ← ESTABLECER ESTADO AQUÍ

        // Crear items de evaluación basados en la prueba
        List<EvaluacionItem> itemsEvaluacion = prueba.getItems().stream()
        .map(itemPrueba -> {
            EvaluacionItem item = new EvaluacionItem();
            item.setItem(itemPrueba); // Asegúrate que Item tiene relación con Prueba
            item.setEvaluacion(evaluacion); // Establecer la relación bidireccional
            item.setValoracion(0); // Valor inicial
            item.setDescription(itemPrueba.getDescription());
            return item;
        }).toList();

        evaluacion.setItems(itemsEvaluacion); // Asignar items a la evaluación

    
        return evaR.save(evaluacion);
    }


    @Transactional
    public Evaluacion reasignarEvaluacion(Long evaluacionId) {
        // 1. Obtener la evaluación existente
        Evaluacion evaluacion = evaR.findById(evaluacionId)
            .orElseThrow(() -> new EvaluacionNotFoundException(evaluacionId));

        // 2. Obtener especialidad de la prueba asociada
        Specialty specialty = evaluacion.getPrueba().getSpecialty();
        User expertoActual = evaluacion.getUser();

        // 3. Buscar nuevos expertos excluyendo al actual
        List<User> expertosDisponibles = userRepository.findExpertsBySpecialtyExcludingUser(
            specialty.getId(),
            UserRole.EXPERT,
            expertoActual.getId()
        );

        if (expertosDisponibles.isEmpty()) {
            throw new NoExpertsAvailableException(specialty.getId());
        }

        // 4. Seleccionar nuevo experto con menos evaluaciones
        User nuevoExperto = expertosDisponibles.get(0);

        // 5. Actualizar y guardar
        evaluacion.setUser(nuevoExperto);
        return evaR.save(evaluacion);
    }

    // Método para reasignación masiva
    @Transactional
    public void reasignarEvaluacionesDeExperto(Long expertoId) {
        List<Evaluacion> evaluaciones = evaR.findByUserId(expertoId);
        
        evaluaciones.forEach(evaluacion -> {
            try {
                reasignarEvaluacion(evaluacion.getId());
            } catch (NoExpertsAvailableException e) {
                
                 throw new RuntimeException("No se pudo reasignar evaluación " + evaluacion.getId() + ": " + e.getMessage());
            }
        });
    }


    public Map<Long, Long> obtenerEstadisticasReasignacion() {
        List<ReasignacionStats> stats = evaR.getEstadisticasReasignacion();
        
        return stats.stream()
            .collect(Collectors.toMap(
                ReasignacionStats::getExpertoId,
                ReasignacionStats::getTotalReasignaciones
            ));
    }

    public EvaluacionDetailsDTO calcularResultados(Long evaluacionId) {
        Evaluacion evaluacion = evaR.findByIdWithItems(evaluacionId)
            .orElseThrow(() -> new RuntimeException("Evaluación no encontrada"));

        Prueba prueba = evaluacion.getPrueba();
        float maxScore = prueba.getMaxScore();
        float totalObtenido = 0f;

        for (EvaluacionItem item : evaluacion.getItems()) {
            Item itemPrueba = item.getItem();
            float puntajeItem = (item.getValoracion() / 100) * itemPrueba.getWeight();
            totalObtenido += puntajeItem;
        }

        float porcentajeFinal = (totalObtenido / maxScore) * 100;

        evaluacion.setPFinalObtenida(totalObtenido);
        evaluacion.setPorcentajeFinalObtenido(porcentajeFinal);
        
        Evaluacion saved = evaR.save(evaluacion);
        return mapToDetailsDTO(saved);
    }

    public EvaluacionDetailsDTO finalizarEvaluacion(Long evaluacionId) {
        Evaluacion evaluacion = evaR.findById(evaluacionId)
            .orElseThrow(() -> new RuntimeException("Evaluación no encontrada"));
        
        evaluacion.setEstado("FINALIZADA");
        Evaluacion saved = evaR.save(evaluacion);
        return mapToDetailsDTO(saved);
    }

    public List<Evaluacion> findPendientesByUser(String username) {
        return evaR.findByUserUsernameAndEstado(username, "PENDIENTE");
    }

    public List<Evaluacion> findFinalizadasByUser(String username) {
        return evaR.findByUserUsernameAndEstado(username, "FINALIZADA");
    }


    public EvaluacionDetailsDTO getEvaluacionDetails(Long id) {
        Evaluacion evaluacion = evaR.findByIdWithItems(id)
            .orElseThrow(() -> new EvaluacionNotFoundException(id));
        return mapToDetailsDTO(evaluacion);
    }






    public EvaluacionDetailsDTO mapToDetailsDTO(Evaluacion evaluacion) {
    return EvaluacionDetailsDTO.builder()
        .id(evaluacion.getId())
        .pFinalObtenida(evaluacion.getPFinalObtenida())
        .porcentajeFinalObtenido(evaluacion.getPorcentajeFinalObtenido())
        .estado(evaluacion.getEstado())
        .pruebaId(evaluacion.getPrueba().getId())
        .participantId(evaluacion.getParticipant().getId())
        .items(evaluacion.getItems().stream().map(this::mapItemToDTO).toList())
        .build();
    }

    private EvaluacionItemDTO mapItemToDTO(EvaluacionItem item) {
        System.out.println("Item ID: " + item.getId() + ", Description: " + item.getDescription());
        return EvaluacionItemDTO.builder()
            .id(item.getId())
            .description(item.getDescription())
            .valoracion(item.getValoracion())
            .justificacion(item.getJustificacion())
            .itemId(item.getItem().getId())
            .build();
    }
    
    public Evaluacion findByIdWithItems(Long id) {
        
        return evaR.findByIdWithItems(id)
            .orElseThrow(() -> new EvaluacionNotFoundException(id));
    }










































    // En EvaluacionService.java
    public EvaluacionDTO convertToDTO(Evaluacion evaluacion) {
        return EvaluacionDTO.builder()
            .id(evaluacion.getId())
            .estado(evaluacion.getEstado())
            .pFinalObtenida(evaluacion.getPFinalObtenida())
            .porcentajeFinalObtenido(evaluacion.getPorcentajeFinalObtenido())
            .participantId(evaluacion.getParticipant().getId())
            .participantName(evaluacion.getParticipant().getName()) // Asume que Participant tiene getName()
            .pruebaId(evaluacion.getPrueba().getId())
            .pruebaEnunciado(evaluacion.getPrueba().getEnunciado())
            .pruebaMaxScore(evaluacion.getPrueba().getMaxScore())
            .userId(evaluacion.getUser().getId())
            .userUsername(evaluacion.getUser().getUsername())
            .build();
    }

    public EvaluacionItemDTO mapToDTO(EvaluacionItem item) {
        return EvaluacionItemDTO.builder()
            .id(item.getId())
            .valoracion(item.getValoracion())
            .justificacion(item.getJustificacion())
            .itemId(item.getItem().getId())
            .build();
    }

    

    

}
