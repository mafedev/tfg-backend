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
    public ResponseEntity<?> createPatient(@RequestBody PatientRequest request) {
        Patient savedPatient = patientService.createPatient(request);
        return ResponseEntity.status(201).body(savedPatient);
    }

    // -------------------- Obtener detalle paciente --------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientDetails(@PathVariable Long id) {
        PatientListDetailsResponse response = userService.getPatientDetails(id);

        if (response == null) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(response);
    }

    // -------------------- Actualizar paciente --------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody PatientUpdateRequest request) {
        boolean updated = userService.updatePatient(id, request);

        if (!updated) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok().build();
    }
}
