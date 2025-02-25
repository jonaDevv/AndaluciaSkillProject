package com.jrm.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
    
    @Schema(description = "Nombre de usuario", example = "admin@admin.com")
    String username, 
    
    @Schema(description = "Contraseña", example = "123456")
    String password
    
    
) {

   

    

}
