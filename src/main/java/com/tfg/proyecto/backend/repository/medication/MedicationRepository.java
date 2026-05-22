package com.tfg.proyecto.backend.repository.medication;

import com.tfg.proyecto.backend.model.medication.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    Optional<Medication> findByName(String name);
}
