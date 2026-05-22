package com.tfg.proyecto.backend.dto.request.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileUpdateRequest {
    // -------------------- Getters y Setters --------------------
    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    // -------------------- Constructor --------------------
    public ProfileUpdateRequest() {
    }
}