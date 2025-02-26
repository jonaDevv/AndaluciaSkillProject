package com.jrm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.jrm.error.prueba.PruebaNotFoundException;
import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.model.Participant;
import com.jrm.model.Prueba;
import com.jrm.model.Specialty;
import com.jrm.model.User;
import com.jrm.model.UserRole;
import com.jrm.repository.ParticipantRepository;
import com.jrm.repository.PruebaRepository;
import com.jrm.repository.UserRepository;
import com.jrm.service.base.BaseService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PruebaService implements BaseService<Prueba, Long> {

    private final PruebaRepository pruebaRepository;
    private final FileStorageService fileStorageService; // Servicio para almacenar archivos
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final EvaluacionService evaluacionService;

    @Override
    public List<Prueba> findAll() {
        return pruebaRepository.findAll();
    }

    @Override
    public Optional<Prueba> findById(Long id) {
        return pruebaRepository.findById(id);
    }
    @Transactional
    @Override
    public Prueba save(Prueba prueba) {
        // Si la prueba tiene un archivo PDF pendiente de almacenar, se procesa aquí.
        if (prueba.getPdfFile() != null) {
            String pdfUrl = fileStorageService.storeFile(prueba.getPdfFile());
            prueba.setPdfUrl(pdfUrl);
            // Quizás luego limpiar el campo pdfFile si no se persiste en la entidad
            prueba.setPdfFile(null);
        }

        //Guardamos la prueba
        Prueba savedPrueba = pruebaRepository.save(prueba);

        // 2. Obtener participantes y expertos de la misma especialidad
        Specialty specialty = savedPrueba.getSpecialty();
        if (specialty != null) {
            List<Participant> participants = participantRepository.findBySpecialtyId(specialty.getId());
           
            List<User> experts = userRepository.findExpertsBySpecialtyOrderByEvaluationCount(specialty.getId(), UserRole.EXPERT);
            
            if (!participants.isEmpty() && !experts.isEmpty()) {
                participants.forEach(participant -> {
                    evaluacionService.crearEvaluacion(participant, savedPrueba);
                });
            }
        }
        
        return pruebaRepository.save(prueba);
    }

    @Override
    public Prueba update(Long id, Prueba prueba) {
        return pruebaRepository.save(prueba); 
    }

    @Override
    public void delete(Long id) {
        Prueba prueba = pruebaRepository.findById(id)
                                    .orElseThrow(() -> new PruebaNotFoundException(id));
        pruebaRepository.delete(prueba);
    }
}
