package com.tfg.proyecto.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.key}")
    private String jwtKey;

    @Value("${jwt.expiration}")
    private Long tokenExpiration;

    // generar token
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpiration);

        return Jwts.builder()
                .setSubject(username) // a quien pertenece el token
                .setIssuedAt(now) // fecha de creación
                .setExpiration(expiryDate) // expira
                .signWith(SignatureAlgorithm.HS256, jwtKey) // firma el token
                .compact(); // lo vuelve String
    }

    // recibe el token y extrae el username
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(jwtKey) //
                .parseClaimsJws(token) // valida y decodifica el token
                .getBody()
                .getSubject();// devuelve el username
    }

    // valida que el token si tenga un username
    public boolean validateToken(String token) {
        try {
            extractUsername(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
