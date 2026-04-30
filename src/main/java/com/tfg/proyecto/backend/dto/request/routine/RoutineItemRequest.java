package com.tfg.proyecto.backend.dto.request.routine;

import com.tfg.proyecto.backend.model.routine.RoutineItem;

import java.time.LocalTime;

public class RoutineItemRequest {

    private String name;
    private LocalTime time;
    private RoutineItem.ItemType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public RoutineItem.ItemType getType() {
        return type;
    }

    public void setType(RoutineItem.ItemType type) {
        this.type = type;
    }
}
