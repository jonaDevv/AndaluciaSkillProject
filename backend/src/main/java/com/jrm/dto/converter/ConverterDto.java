package com.jrm.dto.converter;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;


import com.jrm.dto.user.UserDTO;
import com.jrm.model.User;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;  

@Component
@RequiredArgsConstructor
public class ConverterDto {

    private final ModelMapper modelMapper;

    // // Inicialización de ModelMapper y configuración de mapeos
    // @PostConstruct
    // public void init() {
        

    //     // Puedes configurar los mapeos específicos si lo deseas
    //     modelMapper.addMappings(new PropertyMap<User, UserDTO>() {
    //         @Override
    //         protected void configure() {
    //             // Configuraciones adicionales de mapeo si es necesario
    //         }
    //     });
    // }

    // Método genérico para convertir cualquier tipo de entidad a cualquier tipo de DTO
    public <T, S> S genericConvert(T entity, Class<S> resulClass) {
        return modelMapper.map(entity, resulClass);
    }
	

	

    


}
