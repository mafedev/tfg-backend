package com.tfg.proyecto.backend.controller.users;

import com.tfg.proyecto.backend.dto.request.patient.PatientRequest;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.service.users.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // ------------------- Crear paciente -------------------
    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody PatientRequest request, @RequestHeader("Authorization") String authHeader) {
        Patient savedPatient = patientService.createPatient(request, authHeader);
        return ResponseEntity.ok(savedPatient);
    }
}