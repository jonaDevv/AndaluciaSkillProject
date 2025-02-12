package com.jrm.dto.converter;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;


import com.jrm.dto.user.UserDTO;
import com.jrm.dto.user.UserResponseDTO;
import com.jrm.model.User;
import com.jrm.model.UserRole;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;  

@Component
@RequiredArgsConstructor
public class UserConverterDTO {

    private final ModelMapper modelMapper;

    // @PostConstruct
	// public void init() {
	// 	modelMapper.addMappings(new PropertyMap<User, UserDTO>() {

	// 		@Override
	// 		protected void configure() {
	// 			map().setSpecialty(source.getSpecialty().getName()); // Nombre de la especialidad
	// 		}
	// 	});
	// }
	

    public UserDTO convert(User user) {
        return modelMapper.map(user, UserDTO.class);
    }



	public UserResponseDTO convertUserEntityToGetUserDto(User user) {
		return UserResponseDTO.builder()
				.dni(user.getDni())
				.nombre(user.getNombre())
				.username(user.getUsername())
				.specialtyName(user.getSpecialty().getName())
				.roles(user.getRoles())
				.build();
	}

}
