package com.tfg.proyecto.backend.dto.response;

import com.tfg.proyecto.backend.entity_model.User;

// DTO para devolver solo la información segura del usuario al iniciar sesión
public class LoginResponse {
    private String username;
    private String email;
    private String role;
    private String phone;

    public LoginResponse(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.phone = user.getPhone();
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }
}
