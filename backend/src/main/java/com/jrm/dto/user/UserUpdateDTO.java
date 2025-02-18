package com.jrm.dto.user;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserUpdateDTO {


    @Pattern(regexp = "^[0-9]{8}[A-Za-z]$")
    private String dni;
    
    
    private String nombre;
    
    
    private String username;

    private String password;
    
    private Long specialtyId;

    
}
