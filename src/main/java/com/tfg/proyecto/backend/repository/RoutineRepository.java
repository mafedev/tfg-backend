package com.tfg.proyecto.backend.repository;

import com.tfg.proyecto.backend.model.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    Optional<Routine> findByPatientId(Long patientId);
}
