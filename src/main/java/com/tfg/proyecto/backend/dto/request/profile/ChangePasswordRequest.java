package com.tfg.proyecto.backend.dto.request.profile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordRequest {
    // -------------------- Getters y Setters --------------------
    private String currentPassword;
    private String newPassword;

    // -------------------- Constructor --------------------
    public ChangePasswordRequest() {
    }
}