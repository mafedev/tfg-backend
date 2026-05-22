package com.tfg.proyecto.backend.service.medication;

import com.tfg.proyecto.backend.dto.response.prescription.PrescriptionTrackingResponse;
import com.tfg.proyecto.backend.model.medication.Prescription;
import com.tfg.proyecto.backend.model.medication.PrescriptionTracking;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.medication.PrescriptionRepository;
import com.tfg.proyecto.backend.repository.medication.PrescriptionTrackingRepository;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import com.tfg.proyecto.backend.security.SecurityUtils;
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
    private final PatientRepository patientRepository;
    private final SecurityUtils securityUtils;

    public PrescriptionTrackingService(PrescriptionTrackingRepository trackingRepository, PrescriptionRepository prescriptionRepository, PatientRepository patientRepository, SecurityUtils securityUtils) {
        this.trackingRepository = trackingRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.securityUtils = securityUtils;
    }

    // -------------------- Obtiene y genera las tomas para cualquier día --------------------
    @Transactional
    public List<PrescriptionTrackingResponse> getPrescriptionTracking(Long patientId, LocalDate date) {

        if (prescriptionRepository.findByPatientId(patientId).isEmpty()) {
            return new ArrayList<>();
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<PrescriptionTrackingResponse> result = new ArrayList<>();

        List<Prescription> prescriptions = getValidPrescriptions(patientId, date);

        for (Prescription p : prescriptions) {
            generateTrackingIfNeeded(p, date, startOfDay, endOfDay);
            List<PrescriptionTracking> trackingList = getTrackingForDay(p, startOfDay, endOfDay);
            result.add(mapToDto(p, trackingList));
        }

        return result;
    }

    private List<Prescription> getValidPrescriptions(Long patientId, LocalDate date) {
        return prescriptionRepository.findByPatientId(patientId).stream().filter(p -> p.getStartDate() != null && p.getEndDate() != null).filter(p -> !date.isBefore(p.getStartDate()) && !date.isAfter(p.getEndDate())).filter(p -> p.getStatus() == Prescription.PrescriptionStatus.ACTIVE).toList();
    }

    private void generateTrackingIfNeeded(Prescription p, LocalDate date, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        List<PrescriptionTracking> trackingList = trackingRepository.findByPrescriptionIdAndScheduledTimeBetween(p.getId(), startOfDay, endOfDay);

        if (!trackingList.isEmpty()) return;
        if (p.getStatus() != Prescription.PrescriptionStatus.ACTIVE) return;

        int frequency = p.getFrequencyHours() != null ? p.getFrequencyHours() : 24;
        if (frequency <= 0) return;

        LocalDate startDate = p.getStartDate();

        LocalDateTime baseTime;

        // Si es el primer día se usa firstDoseTime
        if (date.equals(startDate) && p.getFirstDoseTime() != null) {
            baseTime = LocalDateTime.of(startDate, p.getFirstDoseTime());
        } else {
            baseTime = LocalDateTime.of(startDate, p.getFirstDoseTime() != null ? p.getFirstDoseTime() : java.time.LocalTime.MIDNIGHT);
        }

        while (baseTime.isBefore(startOfDay)) {
            baseTime = baseTime.plusHours(frequency);
        }

        LocalDateTime doseTime = baseTime;

        while (doseTime.isBefore(endOfDay)) {

            PrescriptionTracking t = new PrescriptionTracking();
            t.setPrescription(p);
            t.setScheduledTime(doseTime);
            t.setStatus(PrescriptionTracking.TrackingStatus.PENDING);
            t.setTakenTime(null);

            trackingRepository.save(t);

            doseTime = doseTime.plusHours(frequency);
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
    @Transactional
    public PrescriptionTracking toggleTracking(Long prescriptionId, LocalDateTime scheduledTime) {

        Optional<PrescriptionTracking> optionalTracking = trackingRepository.findByPrescription_IdAndScheduledTime(prescriptionId, scheduledTime);

        if (optionalTracking.isEmpty()) {
            return null;
        }

        PrescriptionTracking tracking = optionalTracking.get();

        if (tracking.getStatus() == PrescriptionTracking.TrackingStatus.TAKEN) {
            tracking.setStatus(PrescriptionTracking.TrackingStatus.PENDING);
            tracking.setTakenTime(null);
        } else {
            tracking.setStatus(PrescriptionTracking.TrackingStatus.TAKEN);
            tracking.setTakenTime(LocalDateTime.now());
        }

        return trackingRepository.save(tracking);
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

        if (optional.isEmpty()) return;

        Prescription p = optional.get();
        generateTrackingIfNeeded(p, today, startOfDay, endOfDay);
    }

    // -------------------- USUARIO ACTUAL --------------------
    public List<PrescriptionTrackingResponse> getPrescriptionTrackingForUser(LocalDate date) {
        Long userId = securityUtils.getCurrentUserId();

        Optional<Patient> patientOpt = patientRepository.findByUserId(userId);

        if (patientOpt.isEmpty()) {
            return new ArrayList<>();
        }

        Long patientId = patientOpt.get().getId();

        return getPrescriptionTracking(patientId, date);
    }
}