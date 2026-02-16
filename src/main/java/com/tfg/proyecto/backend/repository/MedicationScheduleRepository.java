package com.tfg.proyecto.backend.repository;

import com.tfg.proyecto.backend.model.medication.MedicationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationScheduleRepository extends JpaRepository<MedicationSchedule, Long> {
    List<MedicationSchedule> findByPatientId(Long patientId);
}
