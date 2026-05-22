package com.tfg.proyecto.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.key}")
    private String jwtKey; // firma del token

    @Value("${jwt.expiration}")
    private Long tokenExpiration; // cuánto dura el token

    // Genera el token con el id del usuario
    public String generateToken(Long userId) {
        Date now = new Date(); // hora actual
        Date expiryDate = new Date(now.getTime() + tokenExpiration); // hora de expiración

        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // a quien pertenece el token
                .setIssuedAt(now) // cuándo se creó
                .setExpiration(expiryDate) // cuándo expira
                .signWith(SignatureAlgorithm.HS256, jwtKey) // firma el token
                .compact(); // convierte todo en un String
    }

    // Recibe el token y extrae el id del usuario
    public String extractId(String token) {
        return Jwts.parser()
                .setSigningKey(jwtKey)
                .parseClaimsJws(token) // decodifica el token
                .getBody()
                .getSubject();// obtiene el id del usuario
    }

    // Valida el token
    public boolean validateToken(String token) {
        try {
            String id = extractId(token);
            System.out.println("Token válido, id: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("Token inválido: " + e.getMessage());
            return false;
        }
    }

}
