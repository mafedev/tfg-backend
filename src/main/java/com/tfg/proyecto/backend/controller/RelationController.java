package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.CreatePatientByCaregiverRequest;
import com.tfg.proyecto.backend.dto.request.RelationRequest;
import com.tfg.proyecto.backend.dto.response.PatientListResponse;
import com.tfg.proyecto.backend.dto.response.RelationResponse;
import com.tfg.proyecto.backend.model.Relation;
import com.tfg.proyecto.backend.model.users.Caregiver;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.security.JwtUtil;
import com.tfg.proyecto.backend.service.RelationService;
import com.tfg.proyecto.backend.repository.CaregiverRepository;
import com.tfg.proyecto.backend.repository.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/relations")
@CrossOrigin(origins = "*")
public class RelationController {

    private final RelationService relationService;
    private final JwtUtil jwtUtil;
    private final CaregiverRepository caregiverRepository;
    private final PatientRepository patientRepository;

    public RelationController(RelationService relationService, JwtUtil jwtUtil,
                              CaregiverRepository caregiverRepository, PatientRepository patientRepository) {
        this.relationService = relationService;
        this.jwtUtil = jwtUtil;
        this.caregiverRepository = caregiverRepository;
        this.patientRepository = patientRepository;
    }

    // ---------------- Generar relación (cuidador) ----------------
    @PostMapping("/generate")
    public ResponseEntity<?> generateRelation(@RequestHeader("Authorization") String authHeader) {
        Long userId = extractId(authHeader);
        System.out.println("[DEBUG] generateRelation - userId from token: " + userId);

        Relation relation = relationService.createRelation(userId);

        if (relation != null) {
            System.out.println("[DEBUG] Relation generada: code=" + relation.getCode());
            return ResponseEntity.ok(new RelationResponse(relation.getCode()));
        }

        System.out.println("[DEBUG] No se pudo generar la relación para userId=" + userId);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // ---------------- Link paciente con relación ----------------
    @PostMapping("/link")
    public ResponseEntity<Void> linkPatient(@RequestBody RelationRequest request,
                                            @RequestHeader("Authorization") String authHeader) {
        Long userId = extractId(authHeader);
        System.out.println("[DEBUG] linkPatient - userId from token: " + userId);

        Optional<Patient> patientOpt = patientRepository.findByUserId(userId);
        if (patientOpt.isEmpty()) {
            System.out.println("[ERROR] linkPatient - Paciente no encontrado para userId: " + userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Patient patient = patientOpt.get();
        System.out.println("[DEBUG] linkPatient - PatientId: " + patient.getId());

        boolean linked = relationService.linkPatient(request.getCode(), patient.getId());
        if (!linked) {
            System.out.println("[ERROR] linkPatient - No se pudo vincular paciente con code: " + request.getCode());
            return ResponseEntity.badRequest().build();
        }

        System.out.println("[DEBUG] linkPatient - Paciente vinculado correctamente con code: " + request.getCode());
        return ResponseEntity.ok().build();
    }

    // ---------------- Obtener lista de pacientes (cuidador) ----------------
    @GetMapping("/patients")
    public ResponseEntity<List<PatientListResponse>> getPatients(@RequestHeader("Authorization") String authHeader) {
        Long userId = extractId(authHeader);
        System.out.println("[DEBUG] getPatients - userId from token: " + userId);

        Optional<Caregiver> caregiverOpt = caregiverRepository.findByUserId(userId);
        if (caregiverOpt.isEmpty()) {
            System.out.println("[ERROR] getPatients - Caregiver no encontrado para userId: " + userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Caregiver caregiver = caregiverOpt.get();
        System.out.println("[DEBUG] getPatients - CaregiverId: " + caregiver.getId());

        List<PatientListResponse> patientsList = relationService.getPatientsList(caregiver.getId());
        System.out.println("[DEBUG] getPatients - Total pacientes encontrados: " + patientsList.size());

        return ResponseEntity.ok(patientsList);
    }

    // ---------------- Eliminar relación ----------------
    @DeleteMapping("/{patientId}")
    public ResponseEntity<String> deleteRelation(@PathVariable Long patientId,
                                                 @RequestHeader("Authorization") String authHeader) {
        Long userId = extractId(authHeader);
        System.out.println("[DEBUG] deleteRelation - userId from token: " + userId);

        Optional<Caregiver> caregiverOpt = caregiverRepository.findByUserId(userId);
        if (caregiverOpt.isEmpty()) {
            System.out.println("[ERROR] deleteRelation - Caregiver no encontrado para userId: " + userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Caregiver caregiver = caregiverOpt.get();
        boolean deleted = relationService.deleteRelation(caregiver.getId(), patientId);
        System.out.println("[DEBUG] deleteRelation - deleted: " + deleted);

        if (deleted) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }


    // ---------------- Extraer ID del token ----------------
    private Long extractId(String authHeader) {
        String token = authHeader.substring(7);
        Long id = Long.parseLong(jwtUtil.extractId(token));
        System.out.println("[DEBUG] extractId - id extraído del token: " + id);
        return id;
    }
}