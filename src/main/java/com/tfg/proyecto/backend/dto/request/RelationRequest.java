package com.tfg.proyecto.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RelationRequest {
    @NotBlank
    private String code;

    // -------------------- Constructor --------------------
    public RelationRequest() {
    }

    // -------------------- Getters y Setters --------------------
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
