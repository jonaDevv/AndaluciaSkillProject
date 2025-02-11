package com.jrm.dto.user;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class GetUserDto {

    private String dni;
    private String nombre;
    private String username;
     private String specialtyName;
    private Set<String> roles;




}
