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
    @NotBlank
    @Pattern(regexp = "^[0-9]{8}[A-Za-z]$")
    private String dni;
    
    @NotBlank
    private String nombre;
    
 
    @NotBlank
    private String username;
    
    @NotNull
    private Long specialtyId;
}
