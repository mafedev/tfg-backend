package com.tfg.proyecto.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    // Obtiene el ID del usuario autenticado desde el SecurityContext
    // Asume que el JWTAuthFilter guarda el userId como principal
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        return Long.parseLong(authentication.getName());
    }
}