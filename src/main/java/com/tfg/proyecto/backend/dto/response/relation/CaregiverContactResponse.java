package com.tfg.proyecto.backend.dto.response.relation;

import lombok.Getter;

@Getter
public class CaregiverContactResponse {
    private Long id;
    private String username;
    private String phone;

    // -------------------- Constructor --------------------
    public CaregiverContactResponse(Long id, String username, String phone) {
        this.id = id;
        this.username = username;
        this.phone = phone;
    }
}