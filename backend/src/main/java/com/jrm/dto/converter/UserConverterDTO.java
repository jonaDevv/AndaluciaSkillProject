package com.jrm.dto.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import com.jrm.dto.user.UserDTO;
import com.jrm.model.User;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;  

@Component
@RequiredArgsConstructor
public class UserConverterDTO {

    private final ModelMapper modelMapper;

    @PostConstruct
	public void init() {
		modelMapper.addMappings(new PropertyMap<User, UserDTO>() {

			@Override
			protected void configure() {
				map().setSpecialtyName(source.getSpecialty().getName());
		    }
        });
        
	}

    public UserDTO convert(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

}
