package com.tfg.proyecto.backend.controller.relation;

import com.tfg.proyecto.backend.dto.response.patient.PatientListResponse;
import com.tfg.proyecto.backend.dto.response.relation.CaregiverContactResponse;
import com.tfg.proyecto.backend.dto.response.relation.RelationResponse;
import com.tfg.proyecto.backend.service.relation.RelationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relations")
@CrossOrigin(origins = "*") // permite peticiones desde Flutter u otros orígenes
public class RelationController {

    private final RelationService relationService;

    public RelationController(RelationService relationService) {
        this.relationService = relationService;
    }

    // Genera una relación desde el cuidador
    @PostMapping
    public ResponseEntity<?> generateRelation() {
        RelationResponse response = relationService.createRelation();

        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Enlaza al paciente con el cuidador por medio del código creado
    @PostMapping("/link")
    public ResponseEntity<Void> linkPatient(@RequestParam String code) {
        boolean linked = relationService.linkPatient(code);
        if (!linked) { // si no se puede vincular, porque está repetido, o ya existe o similar
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build(); // solo devuelve el código HTTP
    }

    // Endpoint para pedir la lista de los pacientes de un cuidador
    @GetMapping("/patients")
    public ResponseEntity<List<PatientListResponse>> getPatients() {
        return ResponseEntity.ok(relationService.getPatientsList());
    }

    // Elimina la relación
    @DeleteMapping("/{patientId}")
    public ResponseEntity<String> deleteRelation(@PathVariable Long patientId) {
        boolean deleted = relationService.deleteRelation(patientId);

        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/my-caregivers")
    public ResponseEntity<List<CaregiverContactResponse>> getMyCaregivers() {
        return ResponseEntity.ok(relationService.getPatientCaregivers());
    }
}