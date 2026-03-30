package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.dto.request.MedicationTrackingRequest;
import com.tfg.proyecto.backend.model.medication.MedicationTracking;
import com.tfg.proyecto.backend.model.medication.Prescription;
import com.tfg.proyecto.backend.repository.MedicationTrackingRepository;
import com.tfg.proyecto.backend.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MedicationTrackingService {

    private final MedicationTrackingRepository trackingRepository;
    private final PrescriptionRepository prescriptionRepository;

    public MedicationTrackingService(MedicationTrackingRepository trackingRepository,
                                     PrescriptionRepository prescriptionRepository) {
        this.trackingRepository = trackingRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    // -------------------- Obtiene y genera las tomas del día --------------------
    // obtiene las tomas del día para un paciente específico, si las tomas no existen, se generan
    @Transactional
    public List<MedicationTrackingRequest> getTodayTracking(Long patientId) {
        LocalDate today = LocalDate.now(); // obtiene el día actual
        LocalDateTime startOfDay = today.atStartOfDay(); // inicio del día
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay(); // final del día

        // obtiene las prescripciones del paciente
        List<Prescription> prescriptions = prescriptionRepository
                .findByPatientIdAndStatus(patientId, Prescription.PrescriptionStatus.ACTIVE)// busca las que están activas
                .stream()
                .filter(p -> p.getStartDate() != null && p.getEndDate() != null) // que tengan fechas válidas
                .filter(p -> !today.isBefore(p.getStartDate()) && !today.isAfter(p.getEndDate())) // y que está dentro del rango
                .toList(); // lo convierte en una lista

        List<MedicationTrackingRequest> result = new ArrayList<>(); // lista que se envía al front

        // Itera cada prescripción
        for (Prescription p : prescriptions) {
            // Busca si ya hay tomas para hoy
            List<MedicationTracking> trackingList = trackingRepository.findByPrescriptionIdAndScheduledTimeBetween(p.getId(), startOfDay, endOfDay);

            // si no hay tomas, se generan
            if (trackingList.isEmpty()) {

                LocalDateTime doseTime;

                // si la hora de la primera toma existe, la usa, sino la crea en 00:00
                if (p.getFirstDoseTime() != null) {
                    doseTime = LocalDateTime.of(today, p.getFirstDoseTime());
                } else {
                    doseTime = startOfDay;
                }

                // -------------------- Generación de las tomas --------------------
                // mientras la hora de la toma no este después del final del día
                while (doseTime.isBefore(endOfDay)) {
                    MedicationTracking t = new MedicationTracking(); // nueva toma
                    t.setPrescription(p); // agrega la prescripción
                    t.setScheduledTime(doseTime); // hora programada
                    t.setStatus(MedicationTracking.TrackingStatus.PENDING); // estado pendiente por defecto
                    t.setTaken(false); // por defecto está sin tomar
                    trackingRepository.save(t); // la guarda

                    // Empieza a sumar las horas dependiendo de la frecuencia
                    doseTime = doseTime.plusHours(p.getFrequencyHours());
                }

                // vuelve a consultar las tomas creadas
                trackingList = trackingRepository.findByPrescriptionIdAndScheduledTimeBetween(p.getId(), startOfDay, endOfDay);
            }

            // --------- Pasarlas a DTO ---------
            // Aquí se guardan las horas de las tomas ya convertidas a DTO
            List<MedicationTrackingRequest.MedicationTrackingTimeRequest> times = new ArrayList<>();

            // se iteran las tomas
            for (MedicationTracking t : trackingList) {

                // solo se envían los datos necesarios
                times.add(new MedicationTrackingRequest.MedicationTrackingTimeRequest(
                        t.getScheduledTime().toString(), // se pasa la fecha a string
                        t.getTaken(), // se agrega el boolean
                        t.getStatus().name() // y el nombre del estado
                ));
            }

            // Este agrupa la información del medicamento junto con el tiempo de las tomas
            result.add(new MedicationTrackingRequest(p.getId(), p.getMedication().getName(), p.getDose() + " " + p.getDoseUnit().name(), times));
        }

        return result;
    }

    // -------------------- Toggle del seguimiento --------------------
    // Cambia el estado entre true y false, TAKEN o PENDING
    @Transactional
    public MedicationTracking toggleTracking(Long prescriptionId, LocalDateTime scheduledTime) {
        // busca la toma en la tabla
        MedicationTracking tracking = trackingRepository.findByPrescription_IdAndScheduledTime(prescriptionId, scheduledTime).orElse(null);

        // si no hay nada, sale
        if (tracking == null) {
            return null;
        }

        // Si el estado es TAKEN
        if (tracking.getStatus() == MedicationTracking.TrackingStatus.TAKEN) {
            tracking.setStatus(MedicationTracking.TrackingStatus.PENDING); // lo pasa a PENDING
            tracking.setTaken(false); // lo pone a false
            tracking.setTakenTime(null); // y quita el tiempo de la toma
        } else { // si el estado es PENDING
            tracking.setStatus(MedicationTracking.TrackingStatus.TAKEN); // lo pasa a TAKEN
            tracking.setTaken(true); // lo pone a true
            tracking.setTakenTime(LocalDateTime.now()); // y guarda la hora actual
        }
        return trackingRepository.save(tracking); // lo guarda
    }
}