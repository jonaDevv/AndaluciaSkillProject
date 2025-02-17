package com.jrm.dto.converter;

import org.springframework.stereotype.Component;

import com.jrm.dto.prueba.PruebaCreateDto;
import com.jrm.dto.user.UserResponseDTO;
import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.model.Prueba;
import com.jrm.model.User;
import com.jrm.service.SpecialtyService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestConverterDto {

    private final SpecialtyService specialtyService;

    public Prueba convert(PruebaCreateDto t) {
		Prueba test = new Prueba();
        return test.builder()
                .enunciado(t.getEnunciado())
                .maxScore(t.getMaxScore())
                .specialty(specialtyService.findById(t.getSpecialtyId())
                .orElseThrow(() -> new SpecialtyNotFoundException(t.getSpecialtyId())))
                .build();
				
	}

}
