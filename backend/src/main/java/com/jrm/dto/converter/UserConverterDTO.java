package com.jrm.dto.converter;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.jrm.dto.UserDTO;
import com.jrm.model.User;

import lombok.RequiredArgsConstructor;  

@Component
@RequiredArgsConstructor
public class UserConverterDTO {

    private final ModelMapper modelMapper;

    public UserDTO convert(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

}
