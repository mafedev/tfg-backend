package com.tfg.proyecto.backend.dto.response.relation;

import lombok.Getter;

@Getter
public class RelationResponse {
    private String relationCode;

    // -------------------- Constructor --------------------
    public RelationResponse(String relationCode) {
        this.relationCode = relationCode;
    }
}
