package com.tfg.proyecto.backend.controller.auth;

import com.tfg.proyecto.backend.dto.request.auth.LoginRequest;
import com.tfg.proyecto.backend.dto.response.auth.LoginResponse;
import com.tfg.proyecto.backend.dto.request.auth.SignupRequest;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // permite peticiones desde Flutter u otros orígenes
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // -------------------- SIGNUP --------------------
    // Endpoint para registrar un usuario nuevo
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        User user = authService.signup(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo crear el usuario");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // -------------------- LOGIN --------------------
    // Endpoint para iniciar sesión
    @PostMapping("/login")
    // valid activa las validaciones de LoginRequest
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
        return ResponseEntity.ok(response);
    }
}
