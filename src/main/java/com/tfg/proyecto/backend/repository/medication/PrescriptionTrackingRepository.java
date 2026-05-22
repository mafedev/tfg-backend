package com.tfg.proyecto.backend.repository.medication;

import com.tfg.proyecto.backend.model.medication.PrescriptionTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PrescriptionTrackingRepository extends JpaRepository<PrescriptionTracking, Long> {

    // busca los registros de una prescripción en un rango de fechas
    List<PrescriptionTracking> findByPrescriptionIdAndScheduledTimeBetween(
            Long prescriptionId, // id de la prescripción
            LocalDateTime start, // fecha de inicio
            LocalDateTime end // fecha de fin
    );

    // Busca un registro en específico por hora e id
    Optional<PrescriptionTracking> findByPrescription_IdAndScheduledTime(Long prescriptionId, LocalDateTime scheduledTime);

    void deleteByPrescriptionIdAndScheduledTimeBetween(Long prescriptionId, LocalDateTime start, LocalDateTime end);
}