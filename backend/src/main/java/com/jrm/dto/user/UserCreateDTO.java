package com.jrm.dto.user;

import java.util.Set;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.validation.annotation.Validated;

import com.jrm.model.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "DTO para creación de usuarios")
public class UserCreateDTO {

    @Schema(description = "DNI del usuario", example = "12345678A", required = true)
    @NotBlank(message = "El DNI no puede ser nulo")
    @Pattern(regexp = "^[0-9]{8}[A-Za-z]$", message = "El DNI debe tener 8 dígitos seguidos de una letra")
    private String dni;

    @Schema(description = "Nombre completo", example = "Jonathan Ramírez", required = true)
    @NotBlank(message = "El nombre no puede ser nulo")
    private String nombre;

    @Schema(description = "Nombre de usuario único", example = "juan.perez", required = true)
    @NotBlank(message = "El username no puede ser nulo")
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "El username debe ser un correo electronico")
    private String username;

    @Schema(description = "ID de la especialidad asociada", example = "1", required = true)
    @NotBlank(message = "La password no puede ser nulo")
    private String password;
    
    @Schema(description = "ID de la especialidad asociada", example = "1")
    private Long specialtyId;

   
    @Schema(description = "Roles del usuario", example = "[EXPERT, ADMIN]", required = true)
     private Set<UserRole> roles = Set.of(UserRole.EXPERT);

}
