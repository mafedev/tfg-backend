package com.tfg.proyecto.backend.dto.response.profile;

import lombok.Getter;

@Getter
public class ProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;

    // -------------------- Constructor --------------------
    public ProfileResponse() {
    }

    public ProfileResponse(Long id, String username, String email, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }
}