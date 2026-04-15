package com.tfg.proyecto.backend.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Datos que envia el front para iniciar sesión
public class LoginRequest {
    @NotBlank(message = "Ingrese el correo")
    @Email
    private String email;

    @NotBlank
    private String password;

    // -------------------- Constructor --------------------
    public LoginRequest() {
    }

    // -------------------- Getters y Setters --------------------
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
