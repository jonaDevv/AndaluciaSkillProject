package com.jrm.controller;

import com.jrm.dto.user.LoginDto;
import com.jrm.dto.user.LoginRequest;
import com.jrm.model.User;
import com.jrm.security.jwt.JwtUtils;
import com.jrm.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
    //     this.authenticationManager = authenticationManager;
    //     this.jwtUtils = jwtUtils;
    // }

    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    //     Authentication authDTO = authManager.authenticate(

    //             new UsernamePasswordAuthenticationToken(
    //                     loginRequest.username(),
    //                     loginRequest.password()
    //             )
    //     );

    //     Authentication authentication = this.authManager.authenticate(authDTO);//Aqui es donde esto puede romperse y pararse
        
    //     User user = (User) authentication.getPrincipal();
        
    //     String token = jwtUtils.generateTokken(user);
    //     // String token = jwtUtils.generateToken(loginRequest.username());

    //     //  String token = this.jwtUtils.generateTokenn(authentication);
    //     return ResponseEntity.ok(new LoginDto(user.getUsername(), user.getRoles(),token));

       
        
    // }

    @PostMapping("/login")
    public LoginDto login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Recibiendo solicitud de login:");
        System.out.println("Usuario: " + loginRequest.username());
        System.out.println("Contraseña: " + loginRequest.password());

        try {
            // Autenticar una sola vez
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.username(),
                    loginRequest.password()
                )
            );

            System.out.println("Autenticación exitosa para el usuario: " + loginRequest.username());

            User user = (User) authentication.getPrincipal(); // Usar el objeto autenticado directamente
            String token = jwtUtils.generateToken(user);

            System.out.println("Token generado: " + token);
            return new LoginDto(user.getUsername(), user.getRoles(),user.getSpecialty().getName(), token);

        } catch (Exception e) {
            System.out.println("Error durante la autenticación: " + e.getMessage());
            throw e;
        }
    }
   
    // public record JwtResponse(String token) {}
}