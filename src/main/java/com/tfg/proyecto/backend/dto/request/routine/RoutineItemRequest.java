package com.tfg.proyecto.backend.dto.request.routine;

import com.tfg.proyecto.backend.model.routine.RoutineItem;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
public class RoutineItemRequest {
    private String name;
    private LocalTime time;
    private RoutineItem.ItemType type;

    // -------------------- Constructor --------------------
    public RoutineItemRequest() {
    }
}
