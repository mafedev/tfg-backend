package com.tfg.proyecto.backend.repository.routine;

import com.tfg.proyecto.backend.model.routine.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    Optional<Routine> findByPatientId(Long patientId);
}
