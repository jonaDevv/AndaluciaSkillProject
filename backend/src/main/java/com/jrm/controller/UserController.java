package com.jrm.controller;


import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import com.jrm.dto.converter.UserConverterDTO;
import com.jrm.dto.user.UserCreateDTO;

import com.jrm.model.User;
import com.jrm.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserConverterDTO userConverterDTO;


    /*  
     *  GET /users
     *  GET /users/:id
     *  POST /users
     *  PUT /users/:id
     *  DELETE /users/:id
     */


    /*
     * Obtener todos los usuarios
     * 
     * @return List<User>
     */
    @GetMapping("/user")
    public ResponseEntity<?> getAllUsers() {
       
        return Optional.ofNullable(userService.findAll())
            .filter(users -> !users.isEmpty()) // Filtramos si la lista está vacía
            .map(users -> users.stream()
                               .map(userConverterDTO::convert)
                               .toList()) // Convertimos a UserDTO
            .map(ResponseEntity::ok) // Si no está vacío, devolvemos ResponseEntity.ok()
            .orElseGet(() -> ResponseEntity.notFound().build()); // Si está vacío, devolvemos 404
        
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

            // Creamos un Stream de un solo elemento (el usuario)
        return Stream.of(userService.findById(id))
        .filter(Objects::nonNull) // Filtramos si el usuario es nulo
        .map(user -> userConverterDTO.convert(user)) // Convertimos a UserDTO
        .map(ResponseEntity::ok) // Envolvemos en ResponseEntity.ok
        .findFirst() // Tomamos el primer (y único) valor
        .orElseGet(() -> ResponseEntity.notFound().build()); // Si no se encuentra, devolvemos 404
            
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody UserCreateDTO user) {
        
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }
   	

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, user));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {


        try {
            userService.delete(id);  // Intentamos eliminar al usuario

            return ResponseEntity.ok().build();  // 200 OK si se eliminó con éxito
       
        } catch (EntityNotFoundException e) {
            
            return ResponseEntity.notFound().build();  // 404 Not Found si no existe el usuario
        }

    }
    



}
