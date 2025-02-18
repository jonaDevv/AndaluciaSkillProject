package com.jrm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.jrm.dto.converter.ConverterDto;
import com.jrm.dto.converter.UserConverterDTO;
import com.jrm.dto.user.UserCreateDTO;
import com.jrm.dto.user.UserDTO;
import com.jrm.dto.user.UserResponseDTO;
import com.jrm.dto.user.UserUpdateDTO;
import com.jrm.error.ApiError;
import com.jrm.error.participant.ParticipantNotFoundException;
import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.error.user.UserNotFoundException;
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
        
        return Optional.of(users)
            .filter(list -> !list.isEmpty())
            .map(nonEmptyList -> nonEmptyList.stream()
                .map(u -> genericDto.genericConvert(u, UserResponseDTO.class))
                .toList())
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@Valid @PathVariable Long id) {
        return userService.findByIdd(id)
                         .map(userConverterDTO::convert)
                         .map(ResponseEntity::ok)
                         .orElseThrow(() -> new UserNotFoundException(id));
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
        
        
        return userService.findById(id)
        .map(user -> {
            // Actualizar campos básicos
            user.setDni(userDTO.getDni());
            user.setNombre(userDTO.getNombre());
            user.setUsername(userDTO.getUsername());
            
            // Manejar specialty
            Optional.ofNullable(userDTO.getSpecialtyId())
                .ifPresent(specialtyId -> {
                    
                    user.setSpecialty(specialtyService.findById(specialtyId)
                    .orElseThrow(() -> new SpecialtyNotFoundException(specialtyId)));
                });
            
            // Guardar cambios y retornar el usuario actualizado
            return userService.update(id, user); // ¡Aquí falta el return!
        })
        .map(userConverterDTO::convert)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new UserNotFoundException(id));

       
        
       
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@Valid @PathVariable Long id) {
        
        return userService.findById(id)
        .map(u -> {
            userService.delete(id); // Eliminar al usuario
            return ResponseEntity.ok().build(); // Retornar una respuesta vacía con estado 200 OK
        })
        .orElseThrow(() -> new UserNotFoundException(id)); // Lanzar excepción si no se encuentra el usuario
    }

}


// package com.jrm.controller;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;
// import io.swagger.v3.oas.annotations.responses.ApiResponses;
// import io.swagger.v3.oas.annotations.parameters.RequestBody;
// import io.swagger.v3.oas.annotations.media.Content;
// import io.swagger.v3.oas.annotations.media.Schema;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;

// import com.jrm.dto.user.UserCreateDTO;
// import com.jrm.dto.user.UserDTO;
// import com.jrm.dto.user.UserResponseDTO;
// import com.jrm.dto.user.UserUpdateDTO;
// import com.jrm.model.User;
// import com.jrm.service.UserService;
// import com.jrm.service.SpecialtyService;
// import com.jrm.service.ApiErrorService;

// import java.util.List;
// import java.util.Optional;

// @RestController
// @RequestMapping("/users")
// @RequiredArgsConstructor
// public class UserController {

//     private final UserService userService;
//     private final SpecialtyService specialtyService;
//     private final ApiErrorService apiErrorService;

//     @Operation(summary = "Obtener todos los usuarios", description = "Recupera todos los usuarios registrados.")
//     @ApiResponses(value = {
//         @ApiResponse(responseCode = "200", description = "Usuarios recuperados exitosamente"),
//         @ApiResponse(responseCode = "404", description = "No se encontraron usuarios", content = @Content)
//     })
//     @GetMapping
//     public ResponseEntity<?> getAllUsers() {
//         List<User> users = userService.findAll();
//         return Optional.of(users)
//             .filter(list -> !list.isEmpty())
//             .map(nonEmptyList -> nonEmptyList.stream()
//                 .map(u -> new UserResponseDTO(u)) // Se supone que tienes un DTO para la respuesta
//                 .toList())
//             .map(ResponseEntity::ok)
//             .orElseGet(() -> ResponseEntity.notFound().build());
//     }

//     @Operation(summary = "Obtener usuario por ID", description = "Recupera un usuario utilizando su ID único.")
//     @ApiResponses(value = {
//         @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(schema = @Schema(implementation = UserDTO.class))),
//         @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
//     })
//     @GetMapping("/{id}")
//     public ResponseEntity<?> getUserById(@Valid @PathVariable Long id) {
//         return userService.findByIdd(id)
//                          .map(user -> new UserDTO(user)) // Aquí deberías mapear tu DTO
//                          .map(ResponseEntity::ok)
//                          .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//     }

//     @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario en el sistema.")
//     @ApiResponses(value = {
//         @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
//         @ApiResponse(responseCode = "409", description = "El usuario o el DNI ya existen", content = @Content)
//     })
//     @PostMapping
//     public ResponseEntity<?> createUser(@Valid @RequestBody UserCreateDTO userDto) {
//         // Validaciones y creación del usuario
//         User user = new User();
//         // Asignar propiedades al usuario...
        
//         User savedUser = userService.save(user);
//         return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDTO(savedUser));
//     }

//     @Operation(summary = "Actualizar usuario", description = "Actualiza un usuario existente por su ID.")
//     @ApiResponses(value = {
//         @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
//         @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
//     })
//     @PutMapping("/{id}")
//     public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userDTO) {
//         return userService.findById(id)
//         .map(user -> {
//             // Actualizar el usuario
//             user.setDni(userDTO.getDni());
//             user.setNombre(userDTO.getNombre());
//             user.setUsername(userDTO.getUsername());
//             // Manejo de specialty
//             return userService.update(id, user);
//         })
//         .map(updatedUser -> new UserResponseDTO(updatedUser))
//         .map(ResponseEntity::ok)
//         .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//     }

//     @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID.")
//     @ApiResponses(value = {
//         @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente"),
//         @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
//     })
//     @DeleteMapping("/{id}")
//     public ResponseEntity<?> deleteUser(@Valid @PathVariable Long id) {
//         return userService.findById(id)
//         .map(u -> {
//             userService.delete(id);
//             return ResponseEntity.ok().build();
//         })
//         .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//     }
// }

