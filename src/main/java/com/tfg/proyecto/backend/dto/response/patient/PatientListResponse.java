package com.tfg.proyecto.backend.dto.response.patient;

import lombok.Getter;

@Getter
public class PatientListResponse {
    // -------------------- Getters --------------------
    private Long id;
    private String username;
    private String phone;

    // -------------------- Constructor --------------------
    public PatientListResponse(Long id, String username, String phone) {
        this.id = id;
        this.username = username;
        this.phone = phone;
    }
}
