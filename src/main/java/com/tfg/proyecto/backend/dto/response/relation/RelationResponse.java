package com.tfg.proyecto.backend.dto.response.relation;

public class RelationResponse {
    private String relationCode;

    // -------------------- Constructor --------------------
    public RelationResponse(String relationCode) {
        this.relationCode = relationCode;
    }

    // -------------------- Getters --------------------
    public String getRelationCode() {
        return relationCode;
    }
}
