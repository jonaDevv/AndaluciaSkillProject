package com.jrm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
                        .specialty(specialtyService.findById(participant.getSpecialty().getId()).orElse(null))
                        .build();

            return participantRepository.save(nuevoParticipante);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Participant update(Long id, Participant t) {
       
        return participantRepository.save(t);   
    }

    @Override
    public void delete(Long id) {
       
        Participant participant = participantRepository.findById(id)
                                    .orElseThrow(() -> new UserNotFoundException(id));
        participantRepository.delete(participant);

        
    }



}
