package com.jrm.service;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jrm.model.User;
import com.jrm.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
        return this.userService.findByUsername(username).orElseThrow(
                
                () -> new UsernameNotFoundException(username + " no encontrado")
        );
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
		return userService.findById(id);
        // .orElseThrow(() -> new UsernameNotFoundException( id +" no encontrado"));
		
	}

}
