package com.tfg.proyecto.backend.controller.medication;

import com.tfg.proyecto.backend.dto.request.medication.PrescriptionRequest;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.medication.Medication;
import com.tfg.proyecto.backend.model.medication.Prescription;
import com.tfg.proyecto.backend.repository.medication.MedicationRepository;
import com.tfg.proyecto.backend.repository.medication.PrescriptionTrackingRepository;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import com.tfg.proyecto.backend.repository.medication.PrescriptionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin("*")
public class PrescriptionController {

    private final PatientRepository patientRepository;
    private final MedicationRepository medicationRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionTrackingRepository prescriptionTrackingRepository;

    public PrescriptionController(PatientRepository patientRepository, MedicationRepository medicationRepository, PrescriptionRepository prescriptionRepository, PrescriptionTrackingRepository prescriptionTrackingRepository) {
        this.patientRepository = patientRepository;
        this.medicationRepository = medicationRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionTrackingRepository = prescriptionTrackingRepository;
    }

    @PostMapping
    public ResponseEntity<?> createPrescription(@RequestBody PrescriptionRequest request) {

        Optional<Patient> patientOptional = patientRepository.findById(request.getPatientId());
        if (patientOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Paciente no encontrado");
        }

        Optional<Medication> medicationOptional = medicationRepository.findById(request.getMedicationId());
        if (medicationOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Medicamento no encontrado");
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
        prescription.setStatus(request.getStatus());

        prescriptionRepository.save(prescription);

        return ResponseEntity.ok("Prescripción creada");
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getByPatient(@PathVariable Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            return ResponseEntity.badRequest().body("Paciente no encontrado");
        }

        var prescriptions = prescriptionRepository.findByPatientId(patientId);

        return ResponseEntity.ok(prescriptions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        Optional<Prescription> optional = prescriptionRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().body("Prescripción no encontrada");
        }

        Prescription prescription = optional.get();

        prescription.setStatus(Prescription.PrescriptionStatus.CANCELLED);

        prescriptionRepository.save(prescription);

        return ResponseEntity.ok("Prescripción cancelada");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrescription(@PathVariable Long id, @RequestBody PrescriptionRequest request) {

        Optional<Prescription> optionalPrescription = prescriptionRepository.findById(id);
        if (optionalPrescription.isEmpty()) {
            return ResponseEntity.badRequest().body("Prescripción no encontrada");
        }

        Prescription prescription = optionalPrescription.get();

        Optional<Patient> patientOptional = patientRepository.findById(request.getPatientId());
        if (patientOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Paciente no encontrado");
        }

        Optional<Medication> medicationOptional = medicationRepository.findById(request.getMedicationId());
        if (medicationOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Medicamento no encontrado");
        }

        // Actualizamos los campos
        prescription.setPatient(patientOptional.get());
        prescription.setMedication(medicationOptional.get());
        prescription.setDose(request.getDose());
        prescription.setDoseUnit(request.getDoseUnit());
        prescription.setFrequencyHours(request.getFrequencyHours());
        prescription.setStartDate(request.getStartDate());
        prescription.setEndDate(request.getEndDate());
        prescription.setFirstDoseTime(request.getFirstDoseTime());
        prescription.setNotes(request.getNotes());

        // Solo actualizar status si viene en la request
        if (request.getStatus() != null) {
            prescription.setStatus(request.getStatus());
        }

        prescriptionRepository.save(prescription);
        prescriptionTrackingRepository.deleteFutureTracking(id, LocalDateTime.now());

        return ResponseEntity.ok("Prescripción actualizada");
    }
}