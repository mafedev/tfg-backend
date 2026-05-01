package com.tfg.proyecto.backend.service.medication;

import com.tfg.proyecto.backend.dto.request.medication.PrescriptionRequest;
import com.tfg.proyecto.backend.model.medication.Medication;
import com.tfg.proyecto.backend.model.medication.Prescription;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.medication.MedicationRepository;
import com.tfg.proyecto.backend.repository.medication.PrescriptionRepository;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    private final PatientRepository patientRepository;
    private final MedicationRepository medicationRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionTrackingService prescriptionTrackingService;

    public PrescriptionService(PatientRepository patientRepository, MedicationRepository medicationRepository, PrescriptionRepository prescriptionRepository, PrescriptionTrackingService prescriptionTrackingService) {
        this.patientRepository = patientRepository;
        this.medicationRepository = medicationRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionTrackingService = prescriptionTrackingService;
    }

    public Prescription createPrescription(PrescriptionRequest request) {
        Optional<Patient> patientOptional = patientRepository.findById(request.getPatientId());
        if (patientOptional.isEmpty()) {
            return null;
        }

        Optional<Medication> medicationOptional = medicationRepository.findById(request.getMedicationId());
        if (medicationOptional.isEmpty()) {
            return null;
        }

        Prescription prescription = new Prescription();
        prescription.setPatient(patientOptional.get());
        prescription.setMedication(medicationOptional.get());
        prescription.setDose(request.getDose());
        prescription.setDoseUnit(request.getDoseUnit());
        prescription.setStartDate(request.getStartDate());
        prescription.setEndDate(request.getEndDate());
        prescription.setFrequencyHours(request.getFrequencyHours());
        prescription.setFirstDoseTime(request.getFirstDoseTime());
        prescription.setNotes(request.getNotes());

        return prescriptionRepository.save(prescription);
    }

    public List<Prescription> getByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            return null;
        }

        return prescriptionRepository.findByPatientId(patientId);
    }

    @Transactional
    public boolean deletePrescription(Long id) {
        Optional<Prescription> optional = prescriptionRepository.findById(id);

        if (optional.isEmpty()) {
            return false;
        }

        Prescription prescription = optional.get();
        prescription.setStatus(Prescription.PrescriptionStatus.CANCELLED);
        prescriptionRepository.save(prescription);
        prescriptionTrackingService.deleteFutureTracking(id);

        return true;
    }

    @Transactional
    public boolean updatePrescription(Long id, PrescriptionRequest request) {
        Optional<Prescription> optionalPrescription = prescriptionRepository.findById(id);
        if (optionalPrescription.isEmpty()) {
            return false;
        }

        Optional<Patient> patientOptional = patientRepository.findById(request.getPatientId());
        if (patientOptional.isEmpty()) {
            return false;
        }

        Optional<Medication> medicationOptional = medicationRepository.findById(request.getMedicationId());
        if (medicationOptional.isEmpty()) {
            return false;
        }

        Prescription prescription = optionalPrescription.get();

        prescription.setPatient(patientOptional.get());
        prescription.setMedication(medicationOptional.get());
        prescription.setDose(request.getDose());
        prescription.setDoseUnit(request.getDoseUnit());
        prescription.setFrequencyHours(request.getFrequencyHours());
        prescription.setStartDate(request.getStartDate());
        prescription.setEndDate(request.getEndDate());
        prescription.setFirstDoseTime(request.getFirstDoseTime());
        prescription.setNotes(request.getNotes());
        prescriptionRepository.save(prescription);

        prescriptionTrackingService.deleteFutureTracking(id);   // borra futuro
        prescriptionTrackingService.regenerateTodayTracking(id); // borra hoy

        return true;
    }
}
