package com.jrm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jrm.dto.user.UserCreateDTO;
import com.jrm.dto.user.UserEditDTO;
import com.jrm.error.user.UserNotFoundException;
import com.jrm.model.User;
import com.jrm.repository.UserRepository;
import com.jrm.service.base.BaseService;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements BaseService<User, Long> {

    private UserRepository userRepository;
    private SpecialtyService specialtyService;


    @Override
    public List<User> findAll() {
        
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User save(UserCreateDTO user) {
        
        User nuevoUsuario = User.builder()
                .dni(user.getDni())
                .nombre(user.getNombre())
                .username(user.getUsername())
                .password(user.getPassword())
                .specialty(specialtyService.findById(user.getSpecialtyId()).orElse(null))
                .build();
        
        return userRepository.save(nuevoUsuario);
    }

    

    

    @Override
    public User update(Long id, User userEdit) {
        
        return userRepository.findById(id).map(u -> {

            u.setDni(userEdit.getDni());
            u.setNombre(userEdit.getNombre());
            u.setUsername(userEdit.getUsername());
            u.setPassword(userEdit.getPassword());
            u.setSpecialty(specialtyService.findById(userEdit.getSpecialty().getId()).orElse(null));
            
            return userRepository.save(u);
        }).orElseThrow();
    }

    

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                              .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user); // Elimina al usuario si existe
    }

    

   
    
    

}
