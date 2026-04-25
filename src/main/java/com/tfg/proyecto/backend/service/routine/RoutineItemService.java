package com.tfg.proyecto.backend.service.routine;

import com.tfg.proyecto.backend.dto.request.routine.RoutineItemRequest;
import com.tfg.proyecto.backend.model.routine.Routine;
import com.tfg.proyecto.backend.model.routine.RoutineItem;
import com.tfg.proyecto.backend.repository.routine.RoutineItemRepository;
import com.tfg.proyecto.backend.repository.routine.RoutineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutineItemService {

    private final RoutineItemRepository routineItemRepository;
    private final RoutineRepository routineRepository;

    public RoutineItemService(RoutineItemRepository routineItemRepository, RoutineRepository routineRepository) {
        this.routineItemRepository = routineItemRepository;
        this.routineRepository = routineRepository;
    }

    public RoutineItem createItem(Long routineId, RoutineItemRequest request) {

        Routine routine = routineRepository.findById(routineId).orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        RoutineItem item = new RoutineItem();
        item.setRoutine(routine);
        item.setName(request.getName());
        item.setTime(request.getTime());
        item.setType(request.getType());

        return routineItemRepository.save(item);
    }

    public List<RoutineItem> getItemsByRoutine(Long routineId) {
        return routineItemRepository.findByRoutineId(routineId);
    }
}
