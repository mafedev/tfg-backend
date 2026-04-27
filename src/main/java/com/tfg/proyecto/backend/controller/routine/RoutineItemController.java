package com.tfg.proyecto.backend.controller.routine;

import com.tfg.proyecto.backend.dto.request.routine.RoutineItemRequest;
import com.tfg.proyecto.backend.dto.response.routine.RoutineItemResponse;
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

    // ---------------- Crear item ----------------
    @PostMapping("/{routineId}")
    public RoutineItemResponse createItem(@PathVariable Long routineId, @RequestBody RoutineItemRequest request) {
        return routineItemService.createItem(routineId, request);
    }

    // ---------------- Obtener items ----------------
    @GetMapping("/{routineId}")
    public List<RoutineItemResponse> getItems(@PathVariable Long routineId) {
        return routineItemService.getItemsByRoutine(routineId);
    }

    // ---------------- Actualizar/editar un item ----------------
    @PutMapping("/{itemId}")
    public RoutineItemResponse updateItem(@PathVariable Long itemId, @RequestBody RoutineItemRequest request) {
        return routineItemService.updateItem(itemId, request);
    }

    // ---------------- Borrar un item ----------------
    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        routineItemService.deleteItem(itemId);
    }
}
