package com.jrm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jrm.dto.converter.ConverterDto;
import com.jrm.dto.converter.UserConverterDTO;
import com.jrm.dto.user.UserCreateDTO;
import com.jrm.dto.user.UserDTO;
import com.jrm.model.User;
import com.jrm.service.SpecialtyService;
import com.jrm.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SpecialtyService specialtyService;
    private final UserConverterDTO userConverterDTO;
    private final ConverterDto genericDto;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<UserDTO> userDTOs = users.stream()
                                      .map(userConverterDTO::convert)
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
        if (userDto == null) {
            return ResponseEntity.badRequest().body("El cuerpo de la solicitud no puede ser nulo");
        }
        User user = genericDto.genericConvert(userDto, User.class);
        user.setSpecialty(specialtyService.findById(userDto.getSpecialtyId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @PathVariable Long id, @Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@Valid @PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}