package com.jrm.dto.user;

import java.util.Set;

import lombok.Data;

@Data
public class UserDTO {

    private String dni;
    private String nombre;
    private String specialtyName;
    private Set<String> roles;



}
