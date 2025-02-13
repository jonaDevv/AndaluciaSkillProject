package com.jrm.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.jrm.dto.user.UserCreateDTO;

import com.jrm.error.user.UserNotFoundException;
import com.jrm.model.Specialty;
import com.jrm.model.User;
import com.jrm.model.UserRole;
import com.jrm.repository.UserRepository;
import com.jrm.service.base.BaseService;



import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements BaseService<User, Long> {

    private final UserRepository userRepository;
    private final SpecialtyService specialtyService;
    private final PasswordEncoder  passwordEncoder;


    @Override
    public List<User> findAll() {
        
        return userRepository.findAll();
    }

   /**
	 * Nos permite buscar un usuario por su nombre de usuario
	 * 
	 * @param username
	 * @return
	 */
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
    

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByIdd(Long id) {
        return userRepository.findById(id);
    }

   
    @Override
    public User save(User user) {
        try {
            
            User nuevoUsuario = User.builder()
                    .dni(user.getDni())
                    .nombre(user.getNombre())
                    .username(user.getUsername())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .specialty(specialtyService.findById(user.getSpecialty().getId()).orElse(null))
                    .roles(user.getRoles())
                    .build();

            return userRepository.save(nuevoUsuario);

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al crear el usuario");
        }
    }

   

    @Override
    public User update(Long id, User userEdit) {
        
                    
            return userRepository.save(userEdit);
            
    }

    

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                              .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user); // Elimina al usuario si existe
    }

    

   
    
    

}
