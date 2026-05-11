package com.tfg.proyecto.backend.service.medication;

import com.tfg.proyecto.backend.dto.request.medication.PrescriptionTrackingResponse;
import com.tfg.proyecto.backend.model.relation.Relation;
import com.tfg.proyecto.backend.model.medication.Prescription;
import com.tfg.proyecto.backend.model.medication.PrescriptionTracking;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.relation.RelationRepository;
import com.tfg.proyecto.backend.repository.medication.PrescriptionRepository;
import com.tfg.proyecto.backend.repository.medication.PrescriptionTrackingRepository;
import com.tfg.proyecto.backend.service.notification.NotificationService;
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

    public PrescriptionTrackingService(PrescriptionTrackingRepository trackingRepository, PrescriptionRepository prescriptionRepository, NotificationService notificationService) {
        this.trackingRepository = trackingRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.notificationService = notificationService;
    }

    // -------------------- Obtiene y genera las tomas para cualquier día --------------------
    // obtiene las tomas de cualquier día para un paciente específico, si las tomas no existen, se generan
    @Transactional
    public List<PrescriptionTrackingResponse> getPrescriptionTracking(Long patientId, LocalDate date) {

        if (prescriptionRepository.findByPatientId(patientId).isEmpty()) {
            return new ArrayList<>();
        }

        LocalDateTime startOfDay = date.atStartOfDay(); // inicio del día
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay(); // fin del día

        List<PrescriptionTrackingResponse> result = new ArrayList<>(); // lista que se envía al front

        List<Prescription> prescriptions = getValidPrescriptions(patientId, date);

        for (Prescription p : prescriptions) {
            generateTrackingIfNeeded(p, date, startOfDay, endOfDay);
            List<PrescriptionTracking> trackingList = getTrackingForDay(p, startOfDay, endOfDay);
            result.add(mapToDto(p, trackingList));
        }

        return result;
    }

    private List<Prescription> getValidPrescriptions(Long patientId, LocalDate date) {
        return prescriptionRepository.findByPatientId(patientId).stream().filter(p -> p.getStartDate() != null && p.getEndDate() != null).filter(p -> !date.isBefore(p.getStartDate()) && !date.isAfter(p.getEndDate())).filter(p -> {
            if (p.getStatus() == Prescription.PrescriptionStatus.CANCELLED) {
                return !date.isAfter(LocalDate.now());
            }
            return true;
        }).toList();
    }

    private void generateTrackingIfNeeded(Prescription p, LocalDate date, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        List<PrescriptionTracking> trackingList = trackingRepository.findByPrescriptionIdAndScheduledTimeBetween(p.getId(), startOfDay, endOfDay);

        if (!trackingList.isEmpty()){
            return;
        }

        if (p.getStatus() != Prescription.PrescriptionStatus.ACTIVE){
            return;
        }

        boolean isFirstDay = date.equals(p.getStartDate());

        LocalDateTime doseTime = (isFirstDay && p.getFirstDoseTime() != null) ? LocalDateTime.of(date, p.getFirstDoseTime()) : startOfDay;

        int frequency = p.getFrequencyHours() != null ? p.getFrequencyHours() : 24;

        while (doseTime.isBefore(endOfDay)) {
            PrescriptionTracking t = new PrescriptionTracking();
            t.setPrescription(p);
            t.setScheduledTime(doseTime);
            t.setStatus(PrescriptionTracking.TrackingStatus.PENDING);
            t.setTakenTime(null);

            trackingRepository.save(t);

            doseTime = doseTime.plusHours(frequency);

            if (frequency <= 0){
                break;
            }
        }
    }

    private List<PrescriptionTracking> getTrackingForDay(Prescription p, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return trackingRepository.findByPrescriptionIdAndScheduledTimeBetween(p.getId(), startOfDay, endOfDay);
    }

    private PrescriptionTrackingResponse mapToDto(Prescription p, List<PrescriptionTracking> trackingList) {
        PrescriptionTrackingResponse dto = new PrescriptionTrackingResponse();

        dto.setPrescriptionId(p.getId());
        dto.setMedicationName(p.getMedication().getName());
        dto.setDose(p.getDose() + " " + p.getDoseUnit());

        List<PrescriptionTrackingResponse.PrescriptionTrackingTimeResponse> times = new ArrayList<>();

        for (PrescriptionTracking t : trackingList) {
            var timeDto = new PrescriptionTrackingResponse.PrescriptionTrackingTimeResponse();
            timeDto.setScheduledTime(t.getScheduledTime().toString());
            timeDto.setStatus(t.getStatus().name());

            times.add(timeDto);
        }

        dto.setTimes(times);

        return dto;
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
            tracking.setTakenTime(null); // y quita el tiempo de la toma
        } else { // si el estado es PENDING
            tracking.setStatus(PrescriptionTracking.TrackingStatus.TAKEN); // lo pasa a TAKEN
            tracking.setTakenTime(LocalDateTime.now()); // y guarda la hora actual

            // se obtiene la prescripción asociada
            Prescription p = tracking.getPrescription();

            notificationService.createNotification(p.getPatient().getId(), "Medicamento tomado", "Has tomado " + p.getMedication().getName());
        }

        PrescriptionTracking saved = trackingRepository.save(tracking);
        return saved;
    }

    @Transactional
    public void deleteFutureTracking(Long prescriptionId) {
        LocalDateTime now = LocalDateTime.now();
        List<PrescriptionTracking> list = trackingRepository.findByPrescriptionIdAndScheduledTimeBetween(prescriptionId, now, now.plusYears(10));
        trackingRepository.deleteAll(list);
    }

    @Transactional
    public void regenerateTodayTracking(Long prescriptionId) {
        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        trackingRepository.deleteByPrescriptionIdAndScheduledTimeBetween(prescriptionId, startOfDay, endOfDay);

        Optional<Prescription> optional = prescriptionRepository.findById(prescriptionId);
        if (optional.isEmpty()) {
            return;
        }

        Prescription p = optional.get();
        generateTrackingIfNeeded(p, today, startOfDay, endOfDay);
    }
}