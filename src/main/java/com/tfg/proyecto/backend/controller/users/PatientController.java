package com.tfg.proyecto.backend.controller.users;

import com.tfg.proyecto.backend.dto.request.medication.MedicationTrackingRequest;
import com.tfg.proyecto.backend.dto.request.patient.PatientRequest;
import com.tfg.proyecto.backend.model.Relation;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import com.tfg.proyecto.backend.repository.RelationRepository;
import com.tfg.proyecto.backend.repository.users.UserRepository;
import com.tfg.proyecto.backend.security.JwtUtil;
import com.tfg.proyecto.backend.service.users.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final RelationRepository relationRepository;
    private final JwtUtil jwtUtil;
    private final PatientService patientService;

    public PatientController(PatientRepository patientRepository, UserRepository userRepository, RelationRepository relationRepository, JwtUtil jwtUtil, PatientService patientService) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.relationRepository = relationRepository;
        this.jwtUtil = jwtUtil;
        this.patientService = patientService;
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

    // ------------------- Obtiene el tracking del paciente autenticado -------------------
    @GetMapping("/prescription-tracking")
    public ResponseEntity<?> getMedicationTracking(@RequestParam String date, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Long userId = Long.parseLong(jwtUtil.extractId(token));

            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Verifica que sea paciente, ya que por el momento son los únicos que pueden recibir las notificaciones
            if (user.getRole() != User.Role.PATIENT) {
                return ResponseEntity.status(403).body("Solo los pacientes pueden acceder a su tracking");
            }

            List<MedicationTrackingRequest> result = patientService.getMedicationTrackingForUser(userId, date);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }
}