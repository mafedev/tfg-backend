package com.tfg.proyecto.backend.dto.response.routine;

import com.tfg.proyecto.backend.model.routine.RoutineItemTracking;
import lombok.Getter;

@Getter
public class RoutineItemTrackingResponse {
    private Long id;
    private Long itemId;
    private String name;
    private String time;
    private String type;
    private String status;

    // -------------------- Constructor --------------------
    public RoutineItemTrackingResponse(RoutineItemTracking t) {
        this.id = t.getId();
        this.itemId = t.getRoutineItem().getId();
        this.name = t.getRoutineItem().getName();
        this.time = t.getRoutineItem().getTime().toString();
        this.type = t.getRoutineItem().getType().name();
        this.status = t.getStatus().name();
    }
}