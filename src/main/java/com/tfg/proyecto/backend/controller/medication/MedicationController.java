package com.tfg.proyecto.backend.controller.medication;

import com.tfg.proyecto.backend.dto.request.medication.MedicationRequest;
import com.tfg.proyecto.backend.model.medication.Medication;
import com.tfg.proyecto.backend.service.medication.MedicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
@CrossOrigin("*")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    // -------- Crear medicamento --------
    @PostMapping
    public ResponseEntity<Medication> create(@Valid @RequestBody MedicationRequest request) {
        Medication saved = medicationService.createMedication(request);
        return ResponseEntity.ok(saved);
    }

    // -------- Obtener medicamentos --------
    @GetMapping
    public ResponseEntity<List<Medication>> getAll() {
        return ResponseEntity.ok(medicationService.getAllMedications());
    }
}
