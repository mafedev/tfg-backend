package com.tfg.proyecto.backend.controller.users;

import com.tfg.proyecto.backend.dto.request.patient.PatientRequest;
import com.tfg.proyecto.backend.dto.request.patient.PatientUpdateRequest;
import com.tfg.proyecto.backend.dto.response.patient.PatientListDetailsResponse;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.service.users.PatientService;
import com.tfg.proyecto.backend.service.users.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;

    public PatientController(PatientService patientService, UserService userService) {
        this.patientService = patientService;
        this.userService = userService;
    }

    // ------------------- Crear paciente -------------------
    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody PatientRequest request, @RequestHeader("Authorization") String authHeader) {
        Patient savedPatient = patientService.createPatient(request, authHeader);
        return ResponseEntity.status(201).body(savedPatient);
    }

    // -------------------- Obtener detalle paciente --------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientDetails(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        PatientListDetailsResponse response = userService.getPatientDetails(id, authHeader);

        if (response == null) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(response);
    }

    // -------------------- Actualizar paciente --------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody PatientUpdateRequest request, @RequestHeader("Authorization") String authHeader) {
        boolean updated = userService.updatePatient(id, request, authHeader);

        if (!updated) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok().build();
    }
}
