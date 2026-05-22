package com.tfg.proyecto.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

// se ejecuta una vez por petición
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // inyección de dependencias
    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // lee el header de la petición
        String header = request.getHeader("Authorization");

        // si no hay header o no empieza por Bearer, no lo autentica
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        } else { // si lo tiene entonces quita el Bearer para extraer el token
            String token = header.substring(7);

            // valida el token con la clase jwtUtil
            if (jwtUtil.validateToken(token)) {
                String id = jwtUtil.extractId(token); // extrae el id

                // se crea un objeto para autenticar
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                id, // usuario autenticado
                                null, // contraseña
                                Collections.emptyList() // sin roles
                        );
                SecurityContextHolder.getContext().setAuthentication(auth); // indica que ya está autenticado y lo guarda
            }
            filterChain.doFilter(request, response); // deja pasar la request al siguiente filtro
        }
    }
}
