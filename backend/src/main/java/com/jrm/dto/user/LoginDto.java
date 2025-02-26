package com.jrm.dto.user;

import java.util.Set;
import org.springframework.validation.annotation.Validated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.jrm.model.UserRole;


@Data
@Validated
@NoArgsConstructor @AllArgsConstructor @Builder
public class LoginDto {

    private String username; 
    private Set<UserRole> roles;
    private String specialtyName;
    private String token;
    
    
}