package com.tfg.proyecto.backend.repository;

import com.tfg.proyecto.backend.model.medication.MedicationTracking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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
    Optional<MedicationTracking> findByPrescription_IdAndScheduledTime(Long prescriptionId, LocalDateTime scheduledTime);

    @Modifying
    @Transactional
    @Query("DELETE FROM MedicationTracking t WHERE t.prescription.id = :id AND t.scheduledTime > :now")
    void deleteFutureTracking(Long id, LocalDateTime now);
}