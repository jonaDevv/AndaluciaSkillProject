package com.jrm.dto.user;

import java.util.Set;

import org.springframework.validation.annotation.Validated;

import com.jrm.model.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "DTO de respuesta para usuarios")
public class UserResponseDTO {

    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "DNI del usuario", example = "12345678A")
    private String dni;
    
    @Schema(description = "Nombre del usuario", example = "Jonathan Ramírez")
    private String nombre;

    @Schema(description = "Nombre de usuario único", example = "juan.perez")
    private String username;

    @Schema(description = "ID de la especialidad asociada", example = "1")
    private String specialtyName;  // Nombre de la especialidad

    @Schema(description = "Roles del usuario", example = "[EXPERT, ADMIN]")
    private Set<UserRole> roles;
    
    // Getters y Setters
}