package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.LoginRequest;
import com.tfg.proyecto.backend.dto.response.AccountResponse;
import com.tfg.proyecto.backend.dto.request.SignupRequest;
import com.tfg.proyecto.backend.model.users.Caregiver;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.security.JwtUtil;
import com.tfg.proyecto.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController // devuelve JSON
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // permite peticiones desde Flutter u otros orígenes
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // Constructor con inyección de dependencias
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Endpoint para registrar un usuario nuevo
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        User user = userService.registerUser(request);

        String phone = null;
        String name = null;

        if (user.getRole() == User.Role.PATIENT) {
            phone = userService.getPatientPhoneByUser(user.getId());
            name = userService.getPatientNameByUser(user.getId());
        } else if (user.getRole() == User.Role.CAREGIVER) {
            phone = userService.getCaregiverPhoneByUser(user.getId());
            name = userService.getCaregiverNameByUser(user.getId());
        }

        AccountResponse response = new AccountResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                name,
                phone
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint para login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Optional<User> userOpt = userService.login(request.getEmail(), request.getPassword());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        User user = userOpt.get();
        String token = jwtUtil.generateToken(user.getId());

        // Variables para almacenar info del paciente o cuidador
        String name = null;
        String phone = null;

        if (user.getRole() == User.Role.PATIENT) {
            // Obtener el paciente asociado (User -> Patient)
            Optional<Patient> patientOpt = userService.getPatientByUserId(user.getId());
            if (patientOpt.isPresent()) {
                Patient patient = patientOpt.get();
                name = patient.getName();
                phone = patient.getPhone();
            }
        } else if (user.getRole() == User.Role.CAREGIVER) {
            // Obtener el cuidador asociado (User -> Caregiver)
            Optional<Caregiver> caregiverOpt = userService.getCaregiverByUserId(user.getId());
            if (caregiverOpt.isPresent()) {
                Caregiver caregiver = caregiverOpt.get();
                name = caregiver.getName();
                phone = caregiver.getPhone();
            }
        }

        AccountResponse response = new AccountResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                name,
                phone,
                token
        );

        return ResponseEntity.ok(response);
    }
}
