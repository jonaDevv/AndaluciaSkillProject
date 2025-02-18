package com.jrm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jrm.error.prueba.PruebaNotFoundException;
import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.model.Prueba;
import com.jrm.repository.PruebaRepository;
import com.jrm.service.base.BaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PruebaService  implements BaseService<Prueba, Long> {

    private final PruebaRepository pruebaRepository;
    private final SpecialtyService specialtyService;

    @Override
    public List<Prueba> findAll() {
        
        return pruebaRepository.findAll();
    }

    @Override
    public Optional<Prueba> findById(Long id) {
        
        return pruebaRepository.findById(id);
    }

    @Override
    public Prueba save(Prueba test) {
        
        return pruebaRepository.save(test);
    }

    @Override
    public Prueba update(Long id, Prueba t) {
        
        return pruebaRepository.save(t); 
    }

    @Override
    public void delete(Long id) {
        
        Prueba test = pruebaRepository.findById(id)
                                    .orElseThrow(() -> new PruebaNotFoundException(id));
        pruebaRepository.delete(test);
    }

}
