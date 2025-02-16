package com.jrm.dto.converter;

import org.springframework.stereotype.Component;

import com.jrm.dto.test.TestCreateDto;
import com.jrm.dto.user.UserResponseDTO;
import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.model.Test;
import com.jrm.model.User;
import com.jrm.service.SpecialtyService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestConverterDto {

    private final SpecialtyService specialtyService;

    public Test convert(TestCreateDto t) {
		Test test = new Test();
        return test.builder()
                .statement(t.getStatement())
                .maxScore(t.getMaxScore())
                .specialty(specialtyService.findById(t.getSpecialtyId())
                .orElseThrow(() -> new SpecialtyNotFoundException(t.getSpecialtyId())))
                .build();
				
	}

}
