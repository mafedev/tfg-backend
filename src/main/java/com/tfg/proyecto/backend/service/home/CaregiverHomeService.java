package com.tfg.proyecto.backend.service.home;

import com.tfg.proyecto.backend.dto.response.home.*;
import com.tfg.proyecto.backend.model.appointment.Appointment;
import com.tfg.proyecto.backend.model.medication.*;
import com.tfg.proyecto.backend.model.relation.Relation;
import com.tfg.proyecto.backend.repository.appointment.AppointmentRepository;
import com.tfg.proyecto.backend.repository.medication.*;
import com.tfg.proyecto.backend.repository.relation.RelationRepository;
import com.tfg.proyecto.backend.security.SecurityUtils;
import com.tfg.proyecto.backend.service.medication.PrescriptionTrackingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CaregiverHomeService {

    private final RelationRepository relationRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionTrackingRepository prescriptionTrackingRepository;
    private final PrescriptionTrackingService prescriptionTrackingService;
    private final AppointmentRepository appointmentRepository;
    private final SecurityUtils securityUtils;

    public CaregiverHomeService(RelationRepository relationRepository, PrescriptionRepository prescriptionRepository, PrescriptionTrackingRepository prescriptionTrackingRepository, PrescriptionTrackingService prescriptionTrackingService, AppointmentRepository appointmentRepository, SecurityUtils securityUtils) {
        this.relationRepository = relationRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionTrackingRepository = prescriptionTrackingRepository;
        this.prescriptionTrackingService = prescriptionTrackingService;
        this.appointmentRepository = appointmentRepository;
        this.securityUtils = securityUtils;
    }

    public List<HomeTrackingResponse> getTodayTrackingByCaregiver() {
        Long caregiverId = securityUtils.getCurrentUserId();

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        List<Relation> relations = relationRepository.findByCaregiverIdAndPatientIdIsNotNull(caregiverId);

        List<Long> patientIds = relations.stream().map(r -> r.getPatient().getId()).toList();

        if (patientIds.isEmpty()) {
            return List.of();
        }

        List<Prescription> prescriptions = prescriptionRepository.findByPatientIdIn(patientIds).stream().filter(p -> p.getStatus() == Prescription.PrescriptionStatus.ACTIVE).toList();

        List<HomeTrackingResponse> result = new ArrayList<>();

        for (Long patientId : patientIds) {
            prescriptionTrackingService.getPrescriptionTracking(patientId, today);
        }
        for (Prescription p : prescriptions) {

            List<PrescriptionTracking> trackingList = prescriptionTrackingRepository.findByPrescriptionIdAndScheduledTimeBetween(p.getId(), start, end);

            for (PrescriptionTracking t : trackingList) {

                if (t.getStatus() == PrescriptionTracking.TrackingStatus.TAKEN) {
                    continue;
                }

                if (t.getScheduledTime().isBefore(LocalDateTime.now())) {
                    continue;
                }

                HomeTrackingResponse dto = new HomeTrackingResponse();

                dto.setTrackingId(t.getId());
                dto.setPrescriptionId(p.getId());

                dto.setMedicationName(p.getMedication().getName());
                dto.setPatientName(p.getPatient().getUser() != null ? p.getPatient().getUser().getUsername() : p.getPatient().getName());

                dto.setScheduledTime(t.getScheduledTime().toString());
                dto.setStatus(t.getStatus().name());

                result.add(dto);
            }
        }
        return result;
    }

    public List<HomeCalendarResponse> getAppointmentDays() {
        Long caregiverId = securityUtils.getCurrentUserId();
        List<Relation> relations = relationRepository.findByCaregiverIdAndPatientIdIsNotNull(caregiverId);
        List<Long> patientIds = relations.stream().map(r -> r.getPatient().getId()).toList();

        if (patientIds.isEmpty()) {
            return List.of();
        }

        LocalDate today = LocalDate.now();

        return appointmentRepository.findByPatientIdIn(patientIds).stream()
                // solo desde hoy
                .filter(a -> !a.getDateTime().toLocalDate().isBefore(today))
                // ignora las canceladas
                .filter(a -> a.getStatus() != Appointment.AppointmentStatus.CANCELLED)
                // quita fechas repetidas
                .map(a -> a.getDateTime().toLocalDate()).distinct().sorted().map(HomeCalendarResponse::new).toList();
    }

    public List<HomeAppointmentDetailResponse> getAppointmentsByDate(LocalDate date) {
        Long caregiverId = securityUtils.getCurrentUserId();
        List<Relation> relations = relationRepository.findByCaregiverIdAndPatientIdIsNotNull(caregiverId);
        List<Long> patientIds = relations.stream().map(r -> r.getPatient().getId()).toList();

        if (patientIds.isEmpty()) {
            return List.of();
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return appointmentRepository.findByPatientIdIn(patientIds).stream().filter(a -> a.getDateTime().isAfter(start) && a.getDateTime().isBefore(end)).filter(a -> a.getStatus() != Appointment.AppointmentStatus.CANCELLED).map(a -> new HomeAppointmentDetailResponse(a.getTitle(), a.getPatient().getUser() != null ? a.getPatient().getUser().getUsername() : a.getPatient().getName(), a.getLocation(), a.getDateTime().toString())).toList();
    }

    public List<HomePendingTrackingResponse> getPendingToday() {
        Long caregiverId = securityUtils.getCurrentUserId();
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = LocalDateTime.now();

        List<Relation> relations = relationRepository.findByCaregiverIdAndPatientIdIsNotNull(caregiverId);

        List<Long> patientIds = relations.stream().map(r -> r.getPatient().getId()).toList();

        if (patientIds.isEmpty()) {
            return List.of();
        }

        List<Prescription> prescriptions = prescriptionRepository.findByPatientIdIn(patientIds).stream().filter(p -> p.getStatus() == Prescription.PrescriptionStatus.ACTIVE).toList();

        List<HomePendingTrackingResponse> result = new ArrayList<>();

        for (Prescription p : prescriptions) {

            List<PrescriptionTracking> trackings = prescriptionTrackingRepository.findByPrescriptionIdAndScheduledTimeBetween(p.getId(), start, end);

            for (PrescriptionTracking t : trackings) {
                if (t.getStatus() != PrescriptionTracking.TrackingStatus.PENDING) {
                    continue;
                }
                HomePendingTrackingResponse dto = new HomePendingTrackingResponse();

                dto.setTrackingId(t.getId());
                dto.setPrescriptionId(p.getId());

                dto.setMedicationName(p.getMedication().getName());

                dto.setPatientName(p.getPatient().getUser() != null ? p.getPatient().getUser().getUsername() : p.getPatient().getName());

                dto.setScheduledTime(t.getScheduledTime().toString());

                result.add(dto);
            }
        }

        return result;
    }
}