package com.tfg.proyecto.backend.dto.response.routine;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
public class RoutineItemResponse {
    private Long id;
    private String name;
    private LocalTime time;
    private String type;

    // -------------------- Constructor --------------------
    public RoutineItemResponse() {
    }

    public RoutineItemResponse(Long id, String name, LocalTime time, String type) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.type = type;
    }
}
