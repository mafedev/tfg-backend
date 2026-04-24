package com.tfg.proyecto.backend.service.medication;

import com.tfg.proyecto.backend.dto.request.medication.PrescriptionTrackingResponse;
import com.tfg.proyecto.backend.model.medication.Prescription;
import com.tfg.proyecto.backend.model.medication.PrescriptionTracking;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.medication.PrescriptionRepository;
import com.tfg.proyecto.backend.repository.medication.PrescriptionTrackingRepository;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import com.tfg.proyecto.backend.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionTrackingService {

    private final PrescriptionTrackingRepository trackingRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final NotificationService notificationService;
    private final PatientRepository patientRepository;

    public PrescriptionTrackingService(PrescriptionTrackingRepository trackingRepository, PrescriptionRepository prescriptionRepository, NotificationService notificationService, PatientRepository patientRepository) {
        this.trackingRepository = trackingRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.notificationService = notificationService;
        this.patientRepository = patientRepository;
    }

    // -------------------- Obtiene y genera las tomas para cualquier día --------------------
    // obtiene las tomas de cualquier día para un paciente específico, si las tomas no existen, se generan
    @Transactional
    public List<PrescriptionTrackingResponse> getPrescriptionTracking(Long userId, LocalDate date) {
        Optional<Patient> optionalPatient = patientRepository.findByUserId(userId);

        // Si no existe paciente asociado a ese usuario no devuelve nada
        if (optionalPatient.isEmpty()) {
            return new ArrayList<>();
        }

        // se obtiene el paciente
        Patient patient = optionalPatient.get();

        // se extrae el id del paciente
        Long patientId = patient.getId();

        LocalDateTime startOfDay = date.atStartOfDay(); // inicio del día
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay(); // fin del día

        // obtiene las prescripciones del paciente
        List<Prescription> prescriptions = prescriptionRepository
                .findByPatientIdAndStatus(patientId, Prescription.PrescriptionStatus.ACTIVE)
                .stream()
                .filter(p -> p.getStartDate() != null && p.getEndDate() != null) // se comprueba que tengan fechas válidas
                .filter(p -> !date.isBefore(p.getStartDate()) && !date.isAfter(p.getEndDate())) // y que está dentro del rango
                .toList(); // lo convierte en una lista

        List<PrescriptionTrackingResponse> result = new ArrayList<>(); // lista que se envía al front

        // Itera cada prescripción
        for (Prescription p : prescriptions) {
            // Busca si ya hay tomas para hoy
            List<PrescriptionTracking> trackingList = trackingRepository.findByPrescriptionIdAndScheduledTimeBetween(p.getId(), startOfDay, endOfDay);

            // si no hay tomas, se generan
            if (trackingList.isEmpty()) {
                boolean isFirstDay = date.equals(p.getStartDate()); // solo el primer día usa la hora de primera dosis

                LocalDateTime doseTime = (isFirstDay && p.getFirstDoseTime() != null) ? LocalDateTime.of(date, p.getFirstDoseTime()) : startOfDay;

                // frecuencia de toma si no existe, se asume cada 24h por seguridad
                int frequency = p.getFrequencyHours() != null ? p.getFrequencyHours() : 24;

                // -------------------- Generación de las tomas --------------------
                // mientras la hora de la toma no este después del final del día
                while (doseTime.isBefore(endOfDay)) {
                    PrescriptionTracking t = new PrescriptionTracking(); // nueva toma
                    t.setPrescription(p); // se asocia la prescripción
                    t.setScheduledTime(doseTime); // hora programada
                    t.setStatus(PrescriptionTracking.TrackingStatus.PENDING); // estado pendiente por defecto
                    t.setTaken(false); // por defecto está sin tomar
                    trackingRepository.save(t); // la guarda

                    // siguiente toma según frecuencia
                    doseTime = doseTime.plusHours(frequency);

                    // seguridad básica para evitar bucles infinitos si la frecuencia es inválida
                    if (frequency <= 0) break;
                }
            }
            // vuelve a consultar las tomas creadas
            trackingList = trackingRepository.findByPrescriptionIdAndScheduledTimeBetween(p.getId(), startOfDay, endOfDay);

            // --------- Pasarlas a DTO ---------
            PrescriptionTrackingResponse dto = new PrescriptionTrackingResponse();

            dto.setPrescriptionId(p.getId()); // id de la prescripción
            dto.setMedicationName(p.getMedication().getName()); // nombre medicamento
            dto.setDose(p.getDose() + " " + p.getDoseUnit()); // dosis + unidad


            // Aquí se guardan las horas de las tomas ya convertidas a DTO
            List<PrescriptionTrackingResponse.PrescriptionTrackingTimeResponse> times = new ArrayList<>();

            // se iteran las tomas
            for (PrescriptionTracking t : trackingList) {
                PrescriptionTrackingResponse.PrescriptionTrackingTimeResponse timeDto = new PrescriptionTrackingResponse.PrescriptionTrackingTimeResponse();
                timeDto.setScheduledTime(t.getScheduledTime().toString()); // se convierte la fecha a String para el frontend
                timeDto.setTaken(t.getTaken());
                timeDto.setStatus(t.getStatus().name());

                times.add(timeDto);
            }
            dto.setTimes(times);
            result.add(dto);
        }

        return result;
    }

    // -------------------- Toggle del seguimiento --------------------
    // Cambia el estado entre true y false, TAKEN o PENDING
    @Transactional
    public PrescriptionTracking toggleTracking(Long prescriptionId, LocalDateTime scheduledTime) {
        Optional<PrescriptionTracking> optionalTracking = trackingRepository.findByPrescription_IdAndScheduledTime(prescriptionId, scheduledTime);

        // si no existe devuelve null
        if (optionalTracking.isEmpty()) {
            return null;
        }

        // Extraemos el tracking
        PrescriptionTracking tracking = optionalTracking.get();

        // Si el estado es TAKEN
        if (tracking.getStatus() == PrescriptionTracking.TrackingStatus.TAKEN) {
            tracking.setStatus(PrescriptionTracking.TrackingStatus.PENDING); // lo pasa a PENDING
            tracking.setTaken(false); // lo pone a false
            tracking.setTakenTime(null); // y quita el tiempo de la toma
        } else { // si el estado es PENDING
            tracking.setStatus(PrescriptionTracking.TrackingStatus.TAKEN); // lo pasa a TAKEN
            tracking.setTaken(true); // lo pone a true
            tracking.setTakenTime(LocalDateTime.now()); // y guarda la hora actual

            // se obtiene la prescripción asociada
            Prescription p = tracking.getPrescription();

            notificationService.createNotification(p.getPatient().getId(), "Medicamento tomado", "Has tomado " + p.getMedication().getName());
        }
        return trackingRepository.save(tracking); // lo guarda
    }
}