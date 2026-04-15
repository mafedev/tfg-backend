package com.tfg.proyecto.backend.dto.response.patient;

public class PatientListResponse {
    private Long id;
    private String username;
    private String phone;

    // -------------------- Constructor --------------------
    public PatientListResponse(Long id, String username, String phone) {
        this.id = id;
        this.username = username;
        this.phone = phone;
    }

    // -------------------- Getters --------------------
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }
}
