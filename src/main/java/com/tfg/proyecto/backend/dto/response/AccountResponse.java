package com.tfg.proyecto.backend.dto.response;

import com.tfg.proyecto.backend.model.users.User.Role;

// DTO para devolver información segura de un usuario recién creado o logueado
public class AccountResponse {
    private Long userId;
    private String username;
    private String email;
    private Role role;
    private String name;
    private String phone;
    private String token;

    // -------------------- Constructor con token --------------------
    public AccountResponse(Long userId, String username, String email, Role role, String name, String phone, String token) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.name = name;
        this.phone = phone;
        this.token = token;
    }

    // Constructor para signup sin token
    public AccountResponse(Long userId, String username, String email, Role role, String name, String phone) {
        this(userId, username, email, role, name, phone, null);
    }

    // -------------------- Getters --------------------
    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }
}