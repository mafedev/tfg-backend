package com.tfg.proyecto.backend.dto.response;

public class RelationResponse {
    private String relationCode;

    public RelationResponse(String code) {
        this.relationCode = code;
    }

    public String getRelationCode() {
        return relationCode;
    }

    public void setRelationCode(String relationCode) {
        this.relationCode = relationCode;
    }
}
