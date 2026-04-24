package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.response.patient.PatientListResponse;
import com.tfg.proyecto.backend.dto.response.RelationResponse;
import com.tfg.proyecto.backend.security.JwtUtil;
import com.tfg.proyecto.backend.service.RelationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relations")
@CrossOrigin(origins = "*") // permite peticiones desde Flutter u otros orígenes
public class RelationController {

    private final RelationService relationService;
    private final JwtUtil jwtUtil;

    public RelationController(RelationService relationService, JwtUtil jwtUtil) {
        this.relationService = relationService;
        this.jwtUtil = jwtUtil;
    }

    // Genera una relación desde el cuidador
    @PostMapping
    public ResponseEntity<?> generateRelation(@RequestHeader("Authorization") String authHeader) {
        Long caregiverId = extractId(authHeader); //obtiene el id
        RelationResponse response = relationService.createRelation(caregiverId);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Enlaza al paciente con el cuidador por medio del código creado
    @PostMapping("/link")
    public ResponseEntity<Void> linkPatient(@RequestParam String code, @RequestHeader("Authorization") String authHeader) {
        Long patientId = extractId(authHeader); // obtiene el id
        boolean linked = relationService.linkPatient(code, patientId); // vincula al paciente usando el código y el id del paciente
        if (!linked) { // si no se puede vincular, porque está repetido, o ya existe o similar
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build(); // solo devuelve el código HTTP
    }

    // Endpoint para pedir la lista de los pacientes de un cuidador
    @GetMapping("/patients")
    public ResponseEntity<List<PatientListResponse>> getPatients(@RequestHeader("Authorization") String authHeader) {
        Long caregiverId = extractId(authHeader); // extrae el id del cuidador

        List<PatientListResponse> patientsList = relationService.getPatientsList(caregiverId);

        return ResponseEntity.ok(patientsList);
    }

    // Elimina la relación
    @DeleteMapping("/{patientId}")
    public ResponseEntity<String> deleteRelation(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {
        Long caregiverId = extractId(authHeader);
        boolean deleted = relationService.deleteRelation(caregiverId, patientId);

        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Extrae el id del token
    private Long extractId(String authHeader) {
        String token = authHeader.substring(7); // extrae el id del token
        return Long.parseLong(jwtUtil.extractId(token));
    }
}
