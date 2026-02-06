package com.tfg.proyecto.backend.repository;

import com.tfg.proyecto.backend.entity_model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    Optional<Medication> findByName(String name);
}
