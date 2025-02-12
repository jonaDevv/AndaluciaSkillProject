package com.jrm.dto.user;

import java.util.Set;

import org.springframework.validation.annotation.Validated;

import com.jrm.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserResponseDTO {
    private Long id;
    private String dni;
    private String nombre;
    private String username;
    private String specialtyName;  // Nombre de la especialidad
    private Set<UserRole> roles;
    
    // Getters y Setters
}