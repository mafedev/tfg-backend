package com.tfg.proyecto.backend.repository;

import com.tfg.proyecto.backend.model.medication.MedicationTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MedicationTrackingRepository extends JpaRepository<MedicationTracking, Long> {

    // busca los registros de una prescripción en un rango de fechas
    List<MedicationTracking> findByPrescriptionIdAndScheduledTimeBetween(
            Long prescriptionId, // id de la prescripción
            LocalDateTime start, // fecha de inicio
            LocalDateTime end // fecha de fin
    );

    // Busca un registro en específico por hora e id
    Optional<MedicationTracking> findByPrescription_IdAndScheduledTime(
            Long prescriptionId,
            LocalDateTime scheduledTime
    );
}