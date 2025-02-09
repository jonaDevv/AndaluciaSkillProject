package com.jrm.service;

import java.util.List;



import com.jrm.model.User;
import com.jrm.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService implements BaseService<User, Long> {

    private UserRepository userRepository;


    @Override
    public List<User> findAll() {
        
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User user) {
        
        return userRepository.findById(id).map(u -> {
            u.setUsername(user.getUsername());
            u.setPassword(user.getPassword());
            return userRepository.save(u);
        }).orElseThrow();
    }

    

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                              .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userRepository.delete(user); // Elimina al usuario si existe
    }

   
    
    

}
