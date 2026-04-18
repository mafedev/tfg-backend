package com.tfg.proyecto.backend.controller.users;

import com.tfg.proyecto.backend.dto.request.patient.PatientUpdateRequest;
import com.tfg.proyecto.backend.dto.response.patient.PatientListDetailsResponse;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.service.users.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // -------------------- Obtener usuario --------------------
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    // -------------------- Obtener detalle paciente --------------------
    @GetMapping("/patients/{id}")
    public ResponseEntity<?> getPatientDetails(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        PatientListDetailsResponse response = userService.getPatientDetails(id, authHeader);

        if (response == null) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(response);
    }

    // -------------------- Actualizar paciente --------------------
    @PutMapping("/patients/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody PatientUpdateRequest request, @RequestHeader("Authorization") String authHeader) {
        boolean updated = userService.updatePatient(id, request, authHeader);

        if (!updated) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok().build();
    }
}
