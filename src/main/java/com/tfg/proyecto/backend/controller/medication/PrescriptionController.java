package com.tfg.proyecto.backend.controller.medication;

import com.tfg.proyecto.backend.dto.request.medication.PrescriptionRequest;
import com.tfg.proyecto.backend.model.medication.Prescription;
import com.tfg.proyecto.backend.service.medication.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin("*")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public ResponseEntity<?> createPrescription(@RequestBody PrescriptionRequest request) {
        Prescription result = prescriptionService.createPrescription(request);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getByPatient(@PathVariable Long patientId) {
        List<Prescription> result = prescriptionService.getByPatient(patientId);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrescription(@PathVariable Long id) {
        boolean deleted = prescriptionService.deletePrescription(id);

        if (!deleted) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok("Prescripción cancelada");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrescription(@PathVariable Long id, @RequestBody PrescriptionRequest request) {
        boolean updated = prescriptionService.updatePrescription(id, request);

        if (!updated) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok("Prescripción actualizada");
    }
}
