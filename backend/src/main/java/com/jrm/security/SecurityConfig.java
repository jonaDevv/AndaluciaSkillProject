package com.jrm.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.jrm.security.jwt.JwtAuthorizationFilter;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final PasswordEncoder passwordEncoder;

    

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = 
        http.getSharedObject(AuthenticationManagerBuilder.class);
        
        authManagerBuilder
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder);

        return authManagerBuilder.build();
    }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .csrf(csrf -> csrf.disable())
    //         .cors(cors -> cors.configurationSource(corsConfigurationSource()))
    //         .exceptionHandling(exception -> 
    //             exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
    //         )
    //         .sessionManagement(session -> 
    //             session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    //         )
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/auth/login","/auth/register" ).permitAll()
    //             .requestMatchers(HttpMethod.GET, "/users", "/specialty").permitAll()
    //             .requestMatchers(HttpMethod.POST, "/auth/**","/auth/**","/users","/specialty").permitAll()
    //             .requestMatchers(HttpMethod.PUT, "/users","/users/**","/specialty/**").permitAll()
    //             .requestMatchers(HttpMethod.DELETE, "/users","/specialty/**").permitAll()


    //             // .requestMatchers(HttpMethod.GET, "/users/**", "/lote/**").hasAuthority("EXPERT")
    //             // .requestMatchers(HttpMethod.POST, "/lote/**").hasAuthority("ADMIN")
    //             // .requestMatchers(HttpMethod.PUT, "/users/**").hasAuthority("ADMIN")
    //             // .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ADMIN")
    //             // .requestMatchers(HttpMethod.POST, "/users/**").hasAnyAuthority("EXPERT", "ADMIN")
    //             // .anyMatch(true).authenticated()
    //             // .anyRequest().authenticated()
    //         )
    //         .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

    //     return http.build();
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .exceptionHandling(exception -> 
                exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers(HttpMethod.POST,"/auth/login", "/auth/register").permitAll()
                
                // Solo ADMIN puede ver lista de Users
                .requestMatchers(HttpMethod.GET, "/users").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/users/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ROLE_ADMIN")
                
                // ADMIN y EXPERT pueden ver lista de Specialty
                .requestMatchers(HttpMethod.GET, "/specialty").hasAnyAuthority("ROLE_ADMIN", "ROLE_EXPERT")
                
                // Operaciones de escritura (solo ADMIN)
                .requestMatchers(HttpMethod.POST, "/participant","/users", "/specialty").permitAll()
                .requestMatchers(HttpMethod.PUT, "/participant/","/users/**", "/specialty/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/participant/","/users", "/specialty/**").hasAuthority("ROLE_ADMIN")
                
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setExposedHeaders(Arrays.asList("Authorization"));
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

     
    
   
}