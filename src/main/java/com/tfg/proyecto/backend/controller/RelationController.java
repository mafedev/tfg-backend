package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.RelationRequest;
import com.tfg.proyecto.backend.dto.response.PatientListResponse;
import com.tfg.proyecto.backend.dto.response.RelationResponse;
import com.tfg.proyecto.backend.entity_model.Relation;
import com.tfg.proyecto.backend.entity_model.User;
import com.tfg.proyecto.backend.repository.UserRepository;
import com.tfg.proyecto.backend.security.JwtUtil;
import com.tfg.proyecto.backend.service.RelationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/relations")
@CrossOrigin(origins = "*") // permite peticiones desde Flutter u otros orígenes
public class RelationController {

    private final RelationService relationService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public RelationController(RelationService relationService, UserRepository userRepository, JwtUtil jwtUtil) {
        this.relationService = relationService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // Genera una relación desde el cuidador
    @PostMapping("/generate")
    public RelationResponse generateRelation(@RequestHeader("Authorization") String header) { // String que contiene el "Bearer + token"
        String token = header.substring(7); // quita "Bearer "
        Long caregiverId = Long.parseLong(jwtUtil.extractId(token)); // extrae el id del token
        Relation relation = relationService.createRelation(caregiverId); // crea la relación desde el service
        return new RelationResponse(relation.getCode()); // Devuelve el código en un RelationResponse
    }

    // Enlaza al paciente con el cuidador por medio del código creado
    @PostMapping("/link")
    public ResponseEntity<Void> linkPatient(@RequestBody RelationRequest request, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // extrae el token
        Long patientId = Long.parseLong(jwtUtil.extractId(token)); // extrae el id del paciente
        relationService.linkPatient(request.getCode(), patientId); // vincula al paciente usando el código y el id del paciente
        return ResponseEntity.ok().build(); // solo devuelve el código HTTP
    }

    // endpoint para pedir la lista de los pacientes de un cuidador
    @GetMapping("/pacients")
    public List<PatientListResponse> getPatients(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // extrae el token
        Long caregiverId = Long.parseLong(jwtUtil.extractId(token)); // extrae el id del cuidador

        //solo cuidadores puedan obtener la lista
        Optional<User> user = userRepository.findById(caregiverId);
        if (user.isPresent() && user.get().getRole().equals("CUIDADOR")) { // si existe y es CUIDADOR
            return relationService.getPatientsList(caregiverId); // entonces devuleve la lista de pacientes
        } else {
            return new ArrayList<>(); // si no, devuelve la lista vacía
        }
    }
}
