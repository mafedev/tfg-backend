package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.MedicationRequest;
import com.tfg.proyecto.backend.model.medication.Medication;
import com.tfg.proyecto.backend.repository.MedicationRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medications")
@CrossOrigin("*")
public class MedicationController {

    private final MedicationRepository medicationRepository;

    public MedicationController(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    // -------- Crear medicamento --------
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody MedicationRequest request) {
        Medication medication = new Medication(
                request.getName(),
                request.getDescription(),
                request.getType()
        );
        return ResponseEntity.ok(medicationRepository.save(medication));
    }


    @GetMapping
    public ResponseEntity<?> getMedication() {
        return ResponseEntity.ok(medicationRepository.findAll());
    }
}
