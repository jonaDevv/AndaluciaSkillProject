package com.jrm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jrm.dto.evaluacionItem.EvaluacionItemDTO;
import com.jrm.error.evaluacionitem.EvaluacionItemNotFoundException;
import com.jrm.model.Evaluacion;
import com.jrm.model.EvaluacionItem;
import com.jrm.repository.EvaluacionItemRepository;
import com.jrm.service.base.BaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluacionItemService  implements BaseService<EvaluacionItem, Long> {
    
    
    
    private final EvaluacionItemRepository evaluacionItemRepository;
    private final EvaluacionService evaluacionService;
    
    @Override 
    public List<EvaluacionItem> findAll() {
        
        return evaluacionItemRepository.findAll();
    }

    @Override
    public Optional<EvaluacionItem> findById(Long id) {
        
        return evaluacionItemRepository.findById(id);
    }

    @Override
    public EvaluacionItem save(EvaluacionItem ev) {
        
        return evaluacionItemRepository.save(ev);
    }

    @Override
    public EvaluacionItem update(Long id, EvaluacionItem ev) {
        return evaluacionItemRepository.save(ev);
        
    }

    @Override
    public void delete(Long id) {
        
        EvaluacionItem ev = evaluacionItemRepository.findById(id)
                                    .orElseThrow(() -> new EvaluacionItemNotFoundException(id));        
        evaluacionItemRepository.delete(ev);
    }

     public EvaluacionItem updateEvaluacionItem(Long itemId, EvaluacionItemDTO dto) {
        EvaluacionItem item = evaluacionItemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        
            
        item.setValoracion(dto.getValoracion());
        item.setJustificacion(dto.getJustificacion());
        
        return evaluacionItemRepository.save(item);
    }

    
}
