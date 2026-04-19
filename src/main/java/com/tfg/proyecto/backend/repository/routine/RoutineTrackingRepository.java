package com.tfg.proyecto.backend.repository.routine;

import com.tfg.proyecto.backend.model.routine.RoutineTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RoutineTrackingRepository extends JpaRepository<RoutineTracking, Long> {
    Optional<RoutineTracking> findByRoutineIdAndDate(Long routineId, LocalDate date);
    void deleteByRoutineId(Long routineId);
}