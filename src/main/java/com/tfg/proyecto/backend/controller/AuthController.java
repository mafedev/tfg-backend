package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.LoginRequest;
import com.tfg.proyecto.backend.dto.response.LoginResponse;
import com.tfg.proyecto.backend.dto.request.SignupRequest;
import com.tfg.proyecto.backend.entity_model.User;
import com.tfg.proyecto.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // permite peticiones desde Flutter u otros orígenes
public class AuthController {

    @Autowired
    private UserService userService;

    // Endpoint para registrar un usuario nuevo
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        User user = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // Endpoint para login
    @PostMapping("/login")
    // valid activa las validaciones de LoginRequest
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Optional<User> user = userService.login(request.getEmail(), request.getPassword());

        if (user.isPresent()) {
            return ResponseEntity.ok(new LoginResponse(user.get()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
}
