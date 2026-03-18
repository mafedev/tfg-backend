package com.tfg.proyecto.backend.dto.response;

import com.tfg.proyecto.backend.model.users.User.Role;
import com.tfg.proyecto.backend.model.users.User;

// DTO para devolver solo la información segura del usuario al iniciar sesión
public class LoginResponse {
    private String username;
    private String email;
    private Role role;
    private String phone;
    private String token;

    // -------------------- Constructor --------------------
    public LoginResponse(String username, String email, Role role, String phone, String token) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.token = token;
    }

    // -------------------- Getters --------------------
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }
}
