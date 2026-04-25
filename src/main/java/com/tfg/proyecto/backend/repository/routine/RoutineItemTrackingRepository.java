package com.tfg.proyecto.backend.repository.routine;

import com.tfg.proyecto.backend.model.routine.RoutineItemTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoutineItemTrackingRepository extends JpaRepository<RoutineItemTracking, Long> {
    boolean existsByRoutineItemIdAndDate(Long itemId, LocalDate date);

    Optional<RoutineItemTracking> findByRoutineItemIdAndDate(Long itemId, LocalDate date);

    List<RoutineItemTracking> findByRoutineItem_Routine_IdAndDate(Long routineId, LocalDate date);
}