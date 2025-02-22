 package com.jrm.dto.converter;

// import java.util.stream.Collectors;

// import org.modelmapper.ModelMapper;
// import org.modelmapper.PropertyMap;
// import org.springframework.stereotype.Component;


// import com.jrm.dto.user.UserDTO;
// import com.jrm.model.User;


// import jakarta.annotation.PostConstruct;
// import lombok.RequiredArgsConstructor;

// @Component
// @RequiredArgsConstructor
// public class ConverterDto {

//     private final ModelMapper modelMapper;

//     // // Inicialización de ModelMapper y configuración de mapeos
//     // @PostConstruct
//     // public void init() {


//     //     // Puedes configurar los mapeos específicos si lo deseas
//     //     modelMapper.addMappings(new PropertyMap<User, UserDTO>() {
//     //         @Override
//     //         protected void configure() {
//     //             // Configuraciones adicionales de mapeo si es necesario
//     //         }
//     //     });
//     // }



//     // Método genérico para convertir cualquier tipo de entidad a cualquier tipo de DTO
//     public <T, S> S genericConvert(T entity, Class<S> resulClass) {
//         return modelMapper.map(entity, resulClass);
//     }









// }

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import com.jrm.dto.item.ItemResponseDto;
import com.jrm.dto.participant.ParticipantResponseDto;
import com.jrm.dto.prueba.PruebaResponseDto;
import com.jrm.dto.user.UserResponseDTO;
import com.jrm.model.Item;
import com.jrm.model.Participant;
import com.jrm.model.Prueba;
import com.jrm.model.User;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConverterDto {

    private final ModelMapper modelMapper;

   @PostConstruct
    public void init() {
        modelMapper.createTypeMap(Participant.class, ParticipantResponseDto.class)
            .addMappings(mapper -> {
                mapper.map(src -> src.getSpecialty().getName(), ParticipantResponseDto::setSpecialtyName);
            });

            modelMapper.createTypeMap(User.class, UserResponseDTO.class)
            .addMappings(mapper -> {
                mapper.map(src -> src.getSpecialty().getName(), UserResponseDTO::setSpecialtyName);
            });
    }

    public <T, S> S genericConvert(T entity, Class<S> resultClass) {
        return modelMapper.map(entity, resultClass);
    }
}


// package com.jrm.dto.converter;

// import org.modelmapper.ModelMapper;
// import org.modelmapper.PropertyMap;
// import org.springframework.stereotype.Component;
// import lombok.RequiredArgsConstructor;
// import com.jrm.dto.item.ItemCreateDto;
// import com.jrm.dto.item.ItemUpdateDto;
// import com.jrm.dto.item.ItemResponseDto;
// import com.jrm.model.Item;
// import com.jrm.model.Prueba;

// import jakarta.annotation.PostConstruct;

// @Component
// @RequiredArgsConstructor
// public class ConverterDto {

//     private final ModelMapper modelMapper;

//     // Inicialización de ModelMapper y configuración de mapeos
//     @PostConstruct
//     public void init() {
//         modelMapper.addMappings(new PropertyMap<ItemCreateDto, Item>() {
//             @Override
//             protected void configure() {
//                 if (source.getPruebaId() != null) {
//                     destination.setPrueba(new Prueba()); // Inicializa el objeto Prueba
//                     map(source.getPruebaId(), destination.getPrueba().getId());
//                 }
//             }
//         });

//         modelMapper.addMappings(new PropertyMap<ItemUpdateDto, Item>() {
//             @Override
//             protected void configure() {
//                 if (source.getPruebaId() != null) {
//                     destination.setPrueba(new Prueba());
//                     map(source.getPruebaId(), destination.getPrueba().getId());
//                 }
//             }
//         });

//         modelMapper.addMappings(new PropertyMap<Item, ItemResponseDto>() {
//             @Override
//             protected void configure() {
//                 if (source.getPrueba() != null) {
//                     map(source.getPrueba().getId(), destination.getPruebaId());
//                 }
//             }
//         });
//     }

//     // Método genérico para convertir entre DTOs y entidades
//     public <T, S> S genericConvert(T entity, Class<S> resultClass) {
//         return modelMapper.map(entity, resultClass);
//     }
// }

