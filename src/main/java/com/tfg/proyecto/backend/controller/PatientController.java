package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.PatientRequest;
import com.tfg.proyecto.backend.model.Relation;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.PatientRepository;
import com.tfg.proyecto.backend.repository.RelationRepository;
import com.tfg.proyecto.backend.repository.UserRepository;
import com.tfg.proyecto.backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final RelationRepository relationRepository;
    private final JwtUtil jwtUtil;

    public PatientController(PatientRepository patientRepository, UserRepository userRepository, RelationRepository relationRepository, JwtUtil jwtUtil) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.relationRepository = relationRepository;
        this.jwtUtil = jwtUtil;
    }

    // ------------------- Crear paciente -------------------
    @PostMapping
    public ResponseEntity<?> createPatient(
            @RequestBody PatientRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {

        // Obtener caregiver del token
        String token = authHeader.substring(7);
        Long caregiverId = Long.parseLong(jwtUtil.extractId(token));

        User caregiver = userRepository.findById(caregiverId)
                .orElseThrow(() -> new RuntimeException("Cuidador no encontrado"));

        // Crear paciente
        Patient patient = new Patient();

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            patient.setUser(user);
        }

        patient.setDiseaseStage(request.getDiseaseStage());
        patient.setNotes(request.getNotes());
        patient.setName(request.getName());
        patient.setPhone(request.getPhone());

        Patient savedPatient = patientRepository.save(patient);

        // Crear relación automática
        Relation relation = new Relation();
        relation.setCaregiver(caregiver);
        relation.setPatient(savedPatient);
        relation.setCode(null);

        relationRepository.save(relation);

        return ResponseEntity.ok(savedPatient);
    }
}