package com.jrm.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.stereotype.Component;

import com.jrm.model.User;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    
    // 1. Añadir logger
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private int expiration;
    
    // 2. Corregir manejo de secret (debe ser Base64)
    private SecretKey getSigningKey() {
        byte[] decodedSecret = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(decodedSecret);
    }

    // public String generateToken(String username) {
    //     return Jwts.builder()
    //             .subject(username)
    //             .issuedAt(new Date())
    //             .expiration(new Date(System.currentTimeMillis() + expiration))
    //             .signWith(getSigningKey())
    //             .compact();
    // }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                // .headerParam("typ", "JWT")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                .signWith(getSigningKey())
                .claim("roles", user.getRoles().stream()
                    .map(role -> "ROLE_" + role.name()) // Agregar prefijo
                    .collect(Collectors.toList()))
                .compact();
    }

    // public String generateTokenn(Authentication authentication) {
    //     User user = ((User) authentication.getPrincipal());
    //     return Jwts.builder()
    //             .subject(username)
    //             .issuedAt(new Date())
    //             .expiration(new Date(System.currentTimeMillis() + expiration))
    //             .signWith(getSigningKey())
    //             .compact();
    // }

    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
        //añadir mas datos con lo que quiera dar acceso a la información del usuario
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    

    // 3. Mejorar validación de token
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token mal formado: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Token vacío o inválido: {}", e.getMessage());
        } catch (SecurityException e) {
            logger.error("Error de seguridad en el token: {}", e.getMessage());
        } catch (JwtException e) {
            logger.error("Error general con el token: {}", e.getMessage());
        }
        return false;
    }
}