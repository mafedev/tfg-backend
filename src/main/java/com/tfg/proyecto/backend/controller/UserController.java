package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.PatientUpdateRequest;
import com.tfg.proyecto.backend.dto.response.PatientListDetailsResponse;
import com.tfg.proyecto.backend.model.users.Caregiver;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.CaregiverRepository;
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
    private final CaregiverRepository caregiverRepository;
    private final JwtUtil jwtUtil;

    public UserController(UserRepository userRepository, UserService userService, RelationRepository relationRepository, PatientRepository patientRepository, CaregiverRepository caregiverRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.relationRepository = relationRepository;
        this.patientRepository = patientRepository;
        this.caregiverRepository = caregiverRepository;
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
    public PatientListDetailsResponse getPatientDetails(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractId(token));

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getRole() == User.Role.CAREGIVER) {
                Optional<Caregiver> caregiverOpt = caregiverRepository.findByUserId(userId);
                if (caregiverOpt.isEmpty()) return null;

                Caregiver caregiver = caregiverOpt.get();

                boolean linked = relationRepository.existsByCaregiverIdAndPatientId(caregiver.getId(), id);
                if (!linked) return null;
                return userService.getPatientDetails(id);
            }
        }
        return null;
    }

    // Permite al cuidador editar los datos del paciente
    @PutMapping("/patients/{id}")
    public ResponseEntity<?> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientUpdateRequest request,
            @RequestHeader("Authorization") String authHeader) {

        // Extraer userId del token
        String token = authHeader.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractId(token));

        // Obtener usuario autenticado
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) return ResponseEntity.status(403).build();

        User currentUser = userOptional.get();

        // Solo caregivers pueden editar
        if (currentUser.getRole() != User.Role.CAREGIVER) {
            return ResponseEntity.status(403).build();
        }

        // Obtiene el caregiver asociado al user
        Optional<Caregiver> caregiverOpt = caregiverRepository.findByUserId(userId);
        if (caregiverOpt.isEmpty()) return ResponseEntity.status(403).build();

        Caregiver caregiver = caregiverOpt.get();

        // Verifica que el caregiver está vinculado al paciente
        boolean linked = relationRepository.existsByCaregiverIdAndPatientId(caregiver.getId(), id);
        if (!linked) return ResponseEntity.status(403).build();

        // Obtener paciente
        Optional<Patient> patientOpt = patientRepository.findById(id);
        if (patientOpt.isEmpty()) return ResponseEntity.notFound().build();

        Patient patient = patientOpt.get();

        // Actualizar campos (solo Patient, ya no User)
        if (request.getName() != null) patient.setName(request.getName());
        if (request.getPhone() != null) patient.setPhone(request.getPhone());
        if (request.getNotes() != null) patient.setNotes(request.getNotes());
        if (request.getDiseaseStage() != null) patient.setDiseaseStage(request.getDiseaseStage());

        // lo guarda
        patientRepository.save(patient);

        return ResponseEntity.ok().build();
    }
}
