package com.tfg.proyecto.backend.service.home;

import com.tfg.proyecto.backend.dto.response.home.HomeTrackingResponse;
import com.tfg.proyecto.backend.model.medication.Prescription;
import com.tfg.proyecto.backend.model.medication.PrescriptionTracking;
import com.tfg.proyecto.backend.model.relation.Relation;
import com.tfg.proyecto.backend.repository.medication.PrescriptionRepository;
import com.tfg.proyecto.backend.repository.medication.PrescriptionTrackingRepository;
import com.tfg.proyecto.backend.repository.relation.RelationRepository;
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

    public CaregiverHomeService(RelationRepository relationRepository, PrescriptionRepository prescriptionRepository, PrescriptionTrackingRepository prescriptionTrackingRepository) {
        this.relationRepository = relationRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionTrackingRepository = prescriptionTrackingRepository;
    }

    public List<HomeTrackingResponse> getTodayTrackingByCaregiver(Long caregiverId) {

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

        for (Prescription p : prescriptions) {

            List<PrescriptionTracking> trackingList = prescriptionTrackingRepository.findByPrescriptionIdAndScheduledTimeBetween(p.getId(), start, end);

            for (PrescriptionTracking t : trackingList) {

                if (t.getStatus() == PrescriptionTracking.TrackingStatus.TAKEN) {
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
}