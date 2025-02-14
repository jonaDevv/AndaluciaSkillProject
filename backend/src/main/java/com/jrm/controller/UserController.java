package com.jrm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jrm.dto.converter.ConverterDto;
import com.jrm.dto.converter.UserConverterDTO;
import com.jrm.dto.user.UserCreateDTO;
import com.jrm.dto.user.UserDTO;
import com.jrm.dto.user.UserResponseDTO;
import com.jrm.dto.user.UserUpdateDTO;
import com.jrm.error.ApiError;
import com.jrm.error.participant.ParticipantNotFoundException;
import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.model.Specialty;
import com.jrm.model.User;
import com.jrm.service.ApiErrorService;
import com.jrm.service.SpecialtyService;
import com.jrm.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SpecialtyService specialtyService;
    private final UserConverterDTO userConverterDTO;
    private final ConverterDto genericDto;
    private final ApiErrorService apiErrorService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<UserResponseDTO> userDTOs = users.stream()
                                      .map(u-> genericDto.genericConvert(u, UserResponseDTO.class))
                                      .toList();
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@Valid @PathVariable Long id) {
        return userService.findByIdd(id)
                         .map(userConverterDTO::convert)
                         .map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }
    

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreateDTO userDto) {
      

        if (userService.findByDni(userDto.getDni()).isPresent()) {
            ApiError apiError = apiErrorService.getErrorMessage("El Dni " + userDto.getDni() + " ya existe");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        }

    
        if (userService.findByUsername(userDto.getUsername()).isPresent()) {
            ApiError apiError = apiErrorService.getErrorMessage("El usuario " + userDto.getUsername() + " ya existe");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        }
        
        User user = genericDto.genericConvert(userDto, User.class);

        user.setSpecialty(specialtyService.findById(userDto.getSpecialtyId())
                        .orElseThrow(() -> new SpecialtyNotFoundException(userDto.getSpecialtyId())));

        User savedUser = userService.save(user);
        
        UserResponseDTO responseDto = genericDto.genericConvert(savedUser, UserResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userDTO) {
        Optional<User> user = userService.findById(id);

        user.ifPresentOrElse(u -> {
            u.setDni(userDTO.getDni());
            u.setNombre(userDTO.getNombre());
            u.setUsername(userDTO.getUsername());

            // Mapear specialtyId a un objeto Specialty=
            if (userDTO.getSpecialtyId() != null) {
                Specialty specialty = new Specialty();
                specialty.setId(userDTO.getSpecialtyId());
                u.setSpecialty(specialty);
            }

        }, ()-> ResponseEntity.notFound().build()); 

        return ResponseEntity.ok(userService.update(id, user.get()));
        
        // user.setDni(userDTO.getDni());
        // user.setNombre(userDTO.getNombre());
        // user.setUsername(userDTO.getUsername());

        // // Mapear specialtyId a un objeto Specialty=
        // if (userDTO.getSpecialtyId() != null) {
        //     Specialty specialty = new Specialty();
        //     specialty.setId(userDTO.getSpecialtyId());
        //     user.setSpecialty(specialty);
        // }
        
        // return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@Valid @PathVariable Long id) {
        try {

            userService.delete(id);
            return ResponseEntity.ok().build();

        } catch (ParticipantNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}