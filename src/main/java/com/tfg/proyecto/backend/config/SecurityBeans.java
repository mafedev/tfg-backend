package com.tfg.proyecto.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Clase de configuración
@Configuration // Indica a Spring que esta clase contiene definiciones de Beans
public class SecurityBeans {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // Se crea una instancia del codificador de contraseñas BCrypt, se crea solo una vez y se reutiliza
        return new BCryptPasswordEncoder();
    }
}
