package com.tfg.proyecto.backend.dto.response.profile;

public class ProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;

    public ProfileResponse() {
    }

    public ProfileResponse(Long id, String username, String email, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}