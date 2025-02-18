package com.jrm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.jrm.error.participant.ParticipantNotFoundException;
import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.error.user.UserNotFoundException;
import com.jrm.model.Participant;
import com.jrm.model.Specialty;
import com.jrm.repository.ParticipantRepository;
import com.jrm.service.base.BaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipantService implements BaseService <Participant,Long>{

    private final ParticipantRepository participantRepository;
    private final SpecialtyService specialtyService;

    @Override
    public List<Participant> findAll() {
        
       return participantRepository.findAll();

    }

    @Override
    public Optional <Participant> findById(Long id){
        
        return participantRepository.findById(id);
    }

    @Override
    public Participant save(Participant participant) {
       
        try {
            Participant nuevoParticipante = 
            Participant.builder()
                        .name(participant.getName())
                        .center(participant.getCenter())
                        .totalScore(participant.getTotalScore())
                        .specialty(specialtyService.findById(participant.getSpecialty().getId())
                        .orElseThrow(()-> new SpecialtyNotFoundException(participant.getSpecialty().getId())))
                        .build();

            return participantRepository.save(nuevoParticipante);

        } catch (Exception e) {
           
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al crear el participante");
        }
    }

    @Override
    public Participant update(Long id, Participant t) {
       
        return participantRepository.save(t);   
    }

    @Override
    public void delete(Long id) {
       
        Participant participant = participantRepository.findById(id)
                                    .orElseThrow(() -> new ParticipantNotFoundException(id));
        participantRepository.delete(participant);

        
    }



}
