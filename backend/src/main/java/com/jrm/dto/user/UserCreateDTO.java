package com.jrm.dto.user;

import lombok.Data;

@Data
public class UserCreateDTO {

    private String dni;
    private String nombre;
    private String username;
    private String password;
    private Long specialtyId;
    private String Role;

}
