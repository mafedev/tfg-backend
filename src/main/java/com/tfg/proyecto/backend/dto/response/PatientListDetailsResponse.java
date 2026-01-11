package com.tfg.proyecto.backend.dto.response;

public class PatientListDetailsResponse {
    private Long id;
    private String username;
    private String phone;

    public PatientListDetailsResponse(Long id, String username, String phone) {
        this.id = id;
        this.username = username;
        this.phone = phone;
    }

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
