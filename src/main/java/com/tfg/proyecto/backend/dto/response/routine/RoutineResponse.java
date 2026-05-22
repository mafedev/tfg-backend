package com.tfg.proyecto.backend.dto.response.routine;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RoutineResponse {
    private Long id;
    private Long patientId;
    private List<RoutineItemResponse> items;

    // -------------------- Constructor --------------------
    public RoutineResponse() {
    }

    public RoutineResponse(Long id, Long patientId, List<RoutineItemResponse> items) {
        this.id = id;
        this.patientId = patientId;
        this.items = items;
    }
}
