package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.RelationRequest;
import com.tfg.proyecto.backend.dto.response.PatientResponse;
import com.tfg.proyecto.backend.dto.response.RelationResponse;
import com.tfg.proyecto.backend.entity_model.Relation;
import com.tfg.proyecto.backend.security.JwtUtil;
import com.tfg.proyecto.backend.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relations")
@CrossOrigin(origins = "*")
public class RelationController {

    private final RelationService relationService;

    public RelationController(RelationService relationService) {
        this.relationService = relationService;
    }

    @Autowired
    private JwtUtil jwtUtil;

    // endpoint para generar la relación desde el cuidador
    @PostMapping("/generate")
    public RelationResponse generateRelation(@RequestHeader("Authorization") String header) { // String que contiene el "Bearer + token"
        String token = header.substring(7); // quita "Bearer "
        Long caregiverId = Long.parseLong(jwtUtil.extractId(token)); // extrae el id del token
        Relation relation = relationService.createRelation(caregiverId); // crea la relación
        return new RelationResponse(relation.getCode()); // Devuelve el código
    }


    // endpoint que enlaza al paciente con el cuidador, por medio del código creado
    @PostMapping("/link")
    public RelationResponse linkPatient(@RequestBody RelationRequest request,
                                        @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long patientId = Long.parseLong(jwtUtil.extractId(token));
        Relation relation = relationService.linkPatient(request.getCode(), patientId);
        return new RelationResponse(relation.getCode());
    }

    // endpoint para pedir la lista de los pacientes
    @GetMapping("/pacients")
    public List<PatientResponse> getPatients(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long caregiverId = Long.parseLong(jwtUtil.extractId(token));
        return relationService.getPatientsList(caregiverId);
    }

}
