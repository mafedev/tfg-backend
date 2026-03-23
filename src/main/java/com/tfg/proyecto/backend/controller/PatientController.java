package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.PatientRequest;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.PatientRepository;
import com.tfg.proyecto.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientController(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    // ------------------- Crear paciente -------------------
    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody PatientRequest request) {
        Patient patient = new Patient();

        // Si se pasa un userId válido, lo asigna
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + request.getUserId()));
            patient.setUser(user);
        }

        // Datos obligatorios / opcionales
        patient.setDiseaseStage(request.getDiseaseStage());
        patient.setNotes(request.getNotes());

        // Datos para pacientes sin cuenta
        patient.setName(request.getName());
        patient.setPhone(request.getPhone());

        // Guardar en la base de datos
        patientRepository.save(patient);

        return ResponseEntity.ok(patient);
    }
}