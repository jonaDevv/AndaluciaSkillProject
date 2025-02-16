package com.jrm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.error.test.TestNotFoundException;
import com.jrm.model.Test;
import com.jrm.repository.TestRepository;
import com.jrm.service.base.BaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestService  implements BaseService<Test, Long> {

    private final TestRepository testRepository;
    private final SpecialtyService specialtyService;

    @Override
    public List<Test> findAll() {
        
        return testRepository.findAll();
    }

    @Override
    public Optional<Test> findById(Long id) {
        
        return testRepository.findById(id);
    }

    @Override
    public Test save(Test test) {
        
        return testRepository.save(test);
    }

    @Override
    public Test update(Long id, Test t) {
        
        return testRepository.save(t); 
    }

    @Override
    public void delete(Long id) {
        
        Test test = testRepository.findById(id)
                                    .orElseThrow(() -> new TestNotFoundException(id));
        testRepository.delete(test);
    }

}
