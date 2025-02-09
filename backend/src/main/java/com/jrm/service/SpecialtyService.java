package com.jrm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jrm.dto.user.UserCreateDTO;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Specialty findById(Long id) {
       
        return specialtyRepository.findById(id).orElse(null);
    }

    @Override
    public Specialty save(UserCreateDTO user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public Specialty update(Long id, Specialty t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
    

}
