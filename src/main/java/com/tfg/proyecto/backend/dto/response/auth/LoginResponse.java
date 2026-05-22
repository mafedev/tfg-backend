package com.tfg.proyecto.backend.dto.response.auth;

import com.tfg.proyecto.backend.model.users.User.Role;
import com.tfg.proyecto.backend.model.users.User;
import lombok.Getter;

// DTO para devolver solo la información segura del usuario al iniciar sesión
@Getter
public class LoginResponse {
    // -------------------- Getters --------------------
    private String username;
    private String email;
    private Role role;
    private String phone;
    private String token;

    // -------------------- Constructor --------------------
    public LoginResponse(User user, String token) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.phone = user.getPhone();
        this.token = token;
    }
}
