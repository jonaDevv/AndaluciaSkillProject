package com.jrm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;


import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.error.user.UserNotFoundException;
import com.jrm.model.Specialty;
import com.jrm.repository.SpecialtyRepository;
import com.jrm.service.base.BaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpecialtyService implements BaseService<Specialty, Long> {

    private final SpecialtyRepository specialtyRepository;

    @Override
    public List<Specialty> findAll() {
        
        return specialtyRepository.findAll();
    }

    @Override
    public Optional<Specialty> findById(Long id) {
       
        return specialtyRepository.findById(id);
    }

    public Optional<Specialty> findByCod(String cod) {
        return specialtyRepository.findByCod(cod);
    }

    @Override
    public Specialty save(Specialty specialty) {
      
        return specialtyRepository.save(Specialty
        
        .builder().name(specialty.getName())
                  .cod(specialty.getCod())
                  .build());
    }

    public Specialty update(Long id, Specialty specialtyUpdate) {
    return specialtyRepository.findById(id)
            .map(existingSpecialty -> {
                existingSpecialty.setCod(specialtyUpdate.getCod());
                existingSpecialty.setName(specialtyUpdate.getName());
                return specialtyRepository.save(existingSpecialty);
            })
            .orElseThrow(() -> new SpecialtyNotFoundException(id)); // Deberías crear esta excepción
    }

    @Override
    public void delete(Long id) {

        Specialty specialty = specialtyRepository.findById(id)
                                    .orElseThrow(() -> new UserNotFoundException(id));
        specialtyRepository.delete(specialty);
       
    }
    
    

}
