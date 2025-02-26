package com.jrm.controller;

import com.jrm.dto.converter.ConverterDto;
import com.jrm.dto.user.LoginDto;
import com.jrm.dto.user.LoginRequest;
import com.jrm.dto.user.UserCreateDTO;
import com.jrm.dto.user.UserResponseDTO;
import com.jrm.error.ApiError;
import com.jrm.error.specialty.SpecialtyNotFoundException;
import com.jrm.model.Specialty;
import com.jrm.model.User;
import com.jrm.security.jwt.JwtUtils;
import com.jrm.service.ApiErrorService;
import com.jrm.service.SpecialtyService;
import com.jrm.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@Tag(name = "Autenticación", description = "Operaciones de login y registro")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ConverterDto genericDto;
    private final ApiErrorService apiErrorService;
    private final SpecialtyService specialtyService;


    
    @Operation(
        summary = "Logearse en AndalucíaSkills",
        description = "Autenticación de usuarios",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Autenticación exitosa",
                content = @Content(schema = @Schema(implementation = LoginDto.class))),
            @ApiResponse(
                responseCode = "401",
                description = "Credenciales inválidas",
                content = @Content(schema = @Schema(implementation = ApiError.class)))
        }
    )
    @PostMapping("/login")
    public LoginDto login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Recibiendo solicitud de login:");
        System.out.println("Usuario: " + loginRequest.username());
        System.out.println("Contraseña: " + loginRequest.password());

        try {
            // Autenticar una sola vez
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.username(),
                    loginRequest.password()
                )
            );

            System.out.println("Autenticación exitosa para el usuario: " + loginRequest.username());

            User user = (User) authentication.getPrincipal(); // Usar el objeto autenticado directamente
            String token = jwtUtils.generateToken(user);

            System.out.println("Token generado: " + token);
            return new LoginDto(user.getUsername(),user.getRoles(),Optional.ofNullable(user.getSpecialty()).map(Specialty::getName).orElse(null), token);

        } catch (Exception e) {
            System.out.println("Error durante la autenticación: " + e.getMessage());
            throw e;
        }
    }



    @Operation(
        summary = "Registro en AndalucíaSkills",
        description = "Registro de usuarios",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Registro exitoso",
                content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(
                responseCode = "401",
                description = "Credenciales inválidas",
                content = @Content(schema = @Schema(implementation = ApiError.class)))
        }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserCreateDTO userDto) {
         if (userService.findByDni(userDto.getDni()).isPresent()) {
            ApiError apiError = apiErrorService.getErrorMessage("El Dni " + userDto.getDni() + " ya existe");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        }

    
        if (userService.findByUsername(userDto.getUsername()).isPresent()) {
            ApiError apiError = apiErrorService.getErrorMessage("El usuario " + userDto.getUsername() + " ya existe");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        }
        
        User user = genericDto.genericConvert(userDto, User.class);

        // Manejar specialty
        Optional.ofNullable(userDto.getSpecialtyId())
                .ifPresent(specialtyId -> {
                    
                    user.setSpecialty(specialtyService.findById(specialtyId).orElseThrow(() -> new SpecialtyNotFoundException(specialtyId)));
                    
                });
        User savedUser = userService.save(user);
        
        UserResponseDTO responseDto = genericDto.genericConvert(savedUser, UserResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }   
    // public record JwtResponse(String token) {}
}