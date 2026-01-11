package com.tfg.proyecto.backend.dto.response;

public class PatientListResponse {
    private Long id;
    private String username;

    public PatientListResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
