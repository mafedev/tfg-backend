package com.tfg.proyecto.backend.service.routine;

import com.tfg.proyecto.backend.dto.request.routine.RoutineItemRequest;
import com.tfg.proyecto.backend.dto.response.routine.RoutineItemResponse;
import com.tfg.proyecto.backend.model.routine.Routine;
import com.tfg.proyecto.backend.model.routine.RoutineItem;
import com.tfg.proyecto.backend.repository.routine.RoutineItemRepository;
import com.tfg.proyecto.backend.repository.routine.RoutineRepository;
import com.tfg.proyecto.backend.repository.routine.RoutineItemTrackingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutineItemService {

    private final RoutineItemRepository routineItemRepository;
    private final RoutineRepository routineRepository;
    private final RoutineItemTrackingRepository trackingRepository;

    public RoutineItemService(RoutineItemRepository routineItemRepository, RoutineRepository routineRepository, RoutineItemTrackingRepository trackingRepository) {
        this.routineItemRepository = routineItemRepository;
        this.routineRepository = routineRepository;
        this.trackingRepository = trackingRepository;
    }

    // ---------------- Crear ----------------
    public RoutineItemResponse createItem(Long routineId, RoutineItemRequest request) {
        Routine routine = routineRepository.findById(routineId).orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        RoutineItem item = new RoutineItem();
        item.setRoutine(routine);
        item.setName(request.getName());
        item.setTime(request.getTime());
        item.setType(request.getType());

        RoutineItem saved = routineItemRepository.save(item);

        return mapToResponse(saved);
    }

    // ---------------- Obtener items ----------------
    public List<RoutineItemResponse> getItemsByRoutine(Long routineId) {
        List<RoutineItem> items = routineItemRepository.findByRoutineId(routineId);
        return items.stream().map(this::mapToResponse).toList();
    }

    // ---------------- Actualizar un item ----------------
    public RoutineItemResponse updateItem(Long itemId, RoutineItemRequest request) {
        RoutineItem item = routineItemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item no encontrado"));
        item.setName(request.getName());
        item.setTime(request.getTime());
        item.setType(request.getType());

        RoutineItem saved = routineItemRepository.save(item);
        return mapToResponse(saved);
    }

    // ---------------- Borrar un item ----------------
    public void deleteItem(Long itemId) {
        RoutineItem item = routineItemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item no encontrado"));

        trackingRepository.deleteAll(trackingRepository.findByRoutineItemId(itemId));

        Routine routine = item.getRoutine();
        if (routine != null) {
            routine.getItems().remove(item);
        }
        routineItemRepository.delete(item);
    }

    private RoutineItemResponse mapToResponse(RoutineItem item) {
        RoutineItemResponse res = new RoutineItemResponse();
        res.setId(item.getId());
        res.setName(item.getName());
        res.setTime(item.getTime());
        res.setType(item.getType().name());
        return res;
    }
}
