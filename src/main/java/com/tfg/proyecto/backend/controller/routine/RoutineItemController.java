package com.tfg.proyecto.backend.controller.routine;

import com.tfg.proyecto.backend.dto.request.routine.RoutineItemRequest;
import com.tfg.proyecto.backend.model.routine.RoutineItem;
import com.tfg.proyecto.backend.service.routine.RoutineItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routine/items")
public class RoutineItemController {

    private final RoutineItemService routineItemService;

    public RoutineItemController(RoutineItemService routineItemService) {
        this.routineItemService = routineItemService;
    }

    @PostMapping("/{routineId}")
    public RoutineItem createItem(@PathVariable Long routineId, @RequestBody RoutineItemRequest request) {
        return routineItemService.createItem(routineId, request);
    }

    @GetMapping("/{routineId}")
    public List<RoutineItem> getItems(@PathVariable Long routineId) {
        return routineItemService.getItemsByRoutine(routineId);
    }
}
