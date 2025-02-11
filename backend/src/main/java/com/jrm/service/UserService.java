package com.jrm.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.jrm.dto.user.UserCreateDTO;

import com.jrm.error.user.UserNotFoundException;
import com.jrm.model.User;
import com.jrm.model.UserRole;
import com.jrm.repository.UserRepository;
import com.jrm.service.base.BaseService;



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

    // public User nuevoUsuario(UserCreateDTO newUser) {

	// 	if (newUser.getPassword().contentEquals(newUser.getPassword2())) {
	// 		User userEntity = User.builder().username(newUser.getUsername())
	// 				.password(passwordEncoder.encode(newUser.getPassword())).avatar(newUser.getAvatar())
	// 				.fullName(newUser.getFullname()).email(newUser.getEmail())
	// 				.roles(Stream.of(UserRole.USER).collect(Collectors.toSet())).build();
	// 		try {
	// 			return save(userEntity);
	// 		} catch (DataIntegrityViolationException ex) {
	// 			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya existe");
	// 		}
	// 	} else {
	// 		throw new NewUserWithDifferentPasswordsException();
	// 	}

	// }

    

    

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
