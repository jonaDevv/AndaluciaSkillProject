package com.jrm.dto.user;

import java.util.Set;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.validation.annotation.Validated;

import com.jrm.model.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserCreateDTO {

    @NotBlank(message = "El DNI no puede ser nulo")
    @Pattern(regexp = "^[0-9]{8}[A-Za-z]$", message = "El DNI debe tener 8 dígitos seguidos de una letra")
    private String dni;

    @NotBlank(message = "El nombre no puede ser nulo")
    private String nombre;

    @NotBlank(message = "El username no puede ser nulo")
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "El username debe ser un correo electronico")
    private String username;

    @NotBlank(message = "La password no puede ser nulo")
    private String password;
    
    private Long specialtyId;

   
     private Set<UserRole> roles = Set.of(UserRole.EXPERT);

}
