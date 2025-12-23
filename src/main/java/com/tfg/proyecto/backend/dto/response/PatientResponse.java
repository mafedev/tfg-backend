package com.tfg.proyecto.backend.dto.response;

public class PatientResponse {
    private Long id;
    private String username;

    public PatientResponse(Long id, String username) {
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
