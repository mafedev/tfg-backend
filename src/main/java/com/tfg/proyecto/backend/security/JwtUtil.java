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

    // Genera el token con el id del cuidador
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpiration);

        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // a quien pertenece el token
                .setIssuedAt(now) // fecha de creación
                .setExpiration(expiryDate) // expira
                .signWith(SignatureAlgorithm.HS256, jwtKey) // firma el token
                .compact(); // lo vuelve String
    }

    // Recibe el token y extrae el username
    public String extractId(String token) {
        return Jwts.parser()
                .setSigningKey(jwtKey)
                .parseClaimsJws(token) // valida y decodifica el token
                .getBody()
                .getSubject();// devuelve el id del usuario
    }

    // Valida el token
    public boolean validateToken(String token) {
        try {
            String id = extractId(token);
            System.out.println("Token válido, id: " + id);
            return true;
        } catch (Exception e) {
            System.out.println("Token inválido: " + e.getMessage());
            return false;
        }
    }

}
