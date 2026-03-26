package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.PatientUpdateRequest;
import com.tfg.proyecto.backend.dto.response.PatientListDetailsResponse;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.PatientRepository;
import com.tfg.proyecto.backend.repository.RelationRepository;
import com.tfg.proyecto.backend.repository.UserRepository;
import com.tfg.proyecto.backend.security.JwtUtil;
import com.tfg.proyecto.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {
    // inyección de dependencias
    private final UserRepository userRepository;
    private final UserService userService;
    private final RelationRepository relationRepository;
    private final PatientRepository patientRepository;
    private final JwtUtil jwtUtil;

    public UserController(UserRepository userRepository, UserService userService, RelationRepository relationRepository, PatientRepository patientRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.relationRepository = relationRepository;
        this.patientRepository = patientRepository;
        this.jwtUtil = jwtUtil;
    }

    // Obtiene un usuario por su id
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        // @PathVariable significa que el valor {id} de la URL se pasa al método como parámetro

        Optional<User> user = userRepository.findById(id); // Se busca el usuario por id

        return user.isPresent()
                ? ResponseEntity.ok(user.get())  // Si se encuentra, lo devolvemos con código 200 OK
                : ResponseEntity.notFound().build(); // Si no, devolvemos un 404 Not Found
    }

    // Devuelve la info del paciente
    @GetMapping("/patients/{id}")
    public ResponseEntity<?> getPatientDetails(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractId(token));

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(403).build();
        }

        User user = userOptional.get();

        if (user.getRole() != User.Role.CAREGIVER) {
            return ResponseEntity.status(403).build();
        }

        boolean linked = relationRepository.existsByCaregiverIdAndPatientId(userId, id);
        if (!linked) {
            return ResponseEntity.status(403).build();
        }

        PatientListDetailsResponse response = userService.getPatientDetails(id);

        return ResponseEntity.ok(response);
    }

    // Permite al cuidador editar los datos del paciente
    @PutMapping("/patients/{id}")
    public ResponseEntity<?> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientUpdateRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractId(token));

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) return ResponseEntity.status(403).build(); // no autorizado

        User currentUser = userOptional.get();
        if (currentUser.getRole() != User.Role.CAREGIVER) return ResponseEntity.status(403).build();

        Optional<Patient> patientOpt = patientRepository.findById(id);
        if (patientOpt.isEmpty()) return ResponseEntity.notFound().build();

        Patient patient = patientOpt.get();
        User user = patient.getUser();

        // ---------- Paciente con cuenta ----------
        if (user != null) {
            if (request.getPhone() != null) user.setPhone(request.getPhone());
            if (request.getNotes() != null) patient.setNotes(request.getNotes());
        } else { // ---------- Paciente sin cuenta ----------
            if (request.getPhone() != null) patient.setPhone(request.getPhone());
            if (request.getNotes() != null) patient.setNotes(request.getNotes());
            if (request.getName() != null) patient.setName(request.getName());
        }

        if (request.getDiseaseStage() != null) patient.setDiseaseStage(request.getDiseaseStage());

        if (user != null) userRepository.save(user);
        patientRepository.save(patient);

        return ResponseEntity.ok().build();
    }
}
