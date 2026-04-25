package com.tfg.proyecto.backend.dto.response.routine;

import com.tfg.proyecto.backend.model.routine.RoutineItemTracking;

public class RoutineItemTrackingResponse {
    private Long id;
    private Long itemId;
    private String name;
    private String time;
    private String type;
    private String status;

    public RoutineItemTrackingResponse(RoutineItemTracking t) {
        this.id = t.getId();
        this.itemId = t.getRoutineItem().getId();
        this.name = t.getRoutineItem().getName();
        this.time = t.getRoutineItem().getTime().toString();
        this.type = t.getRoutineItem().getType().name();
        this.status = t.getStatus().name();
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getId() {
        return id;
    }
}