package com.jrm.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.BaseStream;

import org.springframework.stereotype.Service;

import com.jrm.error.evaluacion.EvaluacionNotFoundException;
import com.jrm.model.Evaluacion;
import com.jrm.model.User;
import com.jrm.repository.EvaluacionRepository;
import com.jrm.service.base.BaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluacionService implements BaseService<Evaluacion, Long> {
    
    private final EvaluacionRepository evaR;
    private final UserService userS;
    private final ParticipantService partS;
    private final PruebaService pruebaS;
  
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


    

    

}
