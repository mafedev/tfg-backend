package com.tfg.proyecto.backend.dto.response;

import com.tfg.proyecto.backend.entity_model.User;

// DTO para devolver solo la información segura del usuario al iniciar sesión
public class LoginResponse {
    private String username;
    private String email;
    private String role;
    private String phone;
    private String token;

    public LoginResponse(User user, String token) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.phone = user.getPhone();
        this.token = token;
    }

    // Getters y Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
