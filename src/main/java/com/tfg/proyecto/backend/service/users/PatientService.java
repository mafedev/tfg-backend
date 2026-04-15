package com.tfg.proyecto.backend.service.users;

import com.tfg.proyecto.backend.dto.request.medication.MedicationTrackingRequest;
import com.tfg.proyecto.backend.dto.request.patient.PatientRequest;
import com.tfg.proyecto.backend.model.medication.PrescriptionTracking;
import com.tfg.proyecto.backend.model.medication.Prescription;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.medication.MedicationTrackingRepository;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import com.tfg.proyecto.backend.repository.medication.PrescriptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicationTrackingRepository trackingRepository;

    public PatientService(PatientRepository patientRepository, PrescriptionRepository prescriptionRepository, MedicationTrackingRepository trackingRepository) {
        this.patientRepository = patientRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.trackingRepository = trackingRepository;
    }

    public Patient createSimplePatient(PatientRequest request) {
        Patient patient = new Patient();
        patient.setUser(null);
        patient.setDiseaseStage(request.getDiseaseStage());
        patient.setNotes(request.getNotes());
        patientRepository.save(patient);
        return patient;
    }

    // Obtiene el tracking de la medicación por susuario y fecha
    @Transactional
    public List<MedicationTrackingRequest> getMedicationTrackingForUser(Long userId, String dateStr) {

        // Busca al paciente
        Patient patient = patientRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // pasa el string a localdate
        LocalDate date = LocalDate.parse(dateStr);
        LocalDateTime startOfDay = date.atStartOfDay(); // inicio del día
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay(); // final del día

        // Obtiene todas las prescripciones activas del paciente para esa fecha
        List<Prescription> prescriptions = prescriptionRepository.findByPatientIdAndStatus(patient.getId(), Prescription.PrescriptionStatus.ACTIVE).stream().filter(p -> p.getStartDate() != null && p.getEndDate() != null).filter(p -> !date.isBefore(p.getStartDate()) && !date.isAfter(p.getEndDate())).toList();

        // lista que se pasa al front
        List<MedicationTrackingRequest> result = new ArrayList<>();

        // itera las prescripciones
        for (Prescription p : prescriptions) {
            // busca las tomas generadas para ese día
            List<PrescriptionTracking> trackingList = trackingRepository.findByPrescriptionIdAndScheduledTimeBetween(p.getId(), startOfDay, endOfDay);

            // Genera las tomas si no existen
            if (trackingList.isEmpty()) {
                LocalDateTime doseTime = p.getFirstDoseTime() != null ? LocalDateTime.of(date, p.getFirstDoseTime()) : startOfDay;

                // las genera hasta el final del día
                while (doseTime.isBefore(endOfDay)) {
                    PrescriptionTracking t = new PrescriptionTracking();
                    t.setPrescription(p);
                    t.setScheduledTime(doseTime);
                    t.setStatus(PrescriptionTracking.TrackingStatus.PENDING);
                    t.setTaken(false);
                    trackingRepository.save(t);
                    doseTime = doseTime.plusHours(p.getFrequencyHours());
                }
                // vuelve a buscar las tomas generadas
                trackingList = trackingRepository.findByPrescriptionIdAndScheduledTimeBetween(p.getId(), startOfDay, endOfDay);
            }

            // convierte a DTO
            List<MedicationTrackingRequest.MedicationTrackingTimeRequest> times = trackingList.stream().map(t -> new MedicationTrackingRequest.MedicationTrackingTimeRequest(t.getScheduledTime().toString(), t.getTaken(), t.getStatus().name())).toList();

            // Agrupa los medicamentos con la dosis y la hora
            result.add(new MedicationTrackingRequest(p.getId(), p.getMedication().getName(), p.getDose() + " " + p.getDoseUnit().name(), times));
        }
        return result; // devuelve toda la info
    }
}