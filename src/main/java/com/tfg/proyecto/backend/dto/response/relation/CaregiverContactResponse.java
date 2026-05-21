package com.tfg.proyecto.backend.dto.response.relation;

public class CaregiverContactResponse {
    private Long id;
    private String username;
    private String phone;

    public CaregiverContactResponse(Long id, String username, String phone) {
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