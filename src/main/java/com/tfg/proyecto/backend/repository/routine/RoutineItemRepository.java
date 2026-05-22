package com.tfg.proyecto.backend.repository.routine;

import com.tfg.proyecto.backend.model.routine.RoutineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutineItemRepository extends JpaRepository<RoutineItem, Long> {

    List<RoutineItem> findByRoutineId(Long routineId);
}