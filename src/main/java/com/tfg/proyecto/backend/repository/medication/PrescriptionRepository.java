package com.tfg.proyecto.backend.repository.medication;

import com.tfg.proyecto.backend.model.medication.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientId(Long patientId);
    List<Prescription> findByPatientIdIn(List<Long> patientIds);
    boolean existsByPatientIdAndMedicationId(Long patientId, Long medicationId);
}
