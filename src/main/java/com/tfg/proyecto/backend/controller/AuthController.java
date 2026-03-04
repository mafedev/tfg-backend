package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.LoginRequest;
import com.tfg.proyecto.backend.dto.response.LoginResponse;
import com.tfg.proyecto.backend.dto.request.SignupRequest;
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
        User user = userService.registerUser(request); // llama al service para registrar al usuario
        // Devuleve 201 al crearlo y al usuario creado en JSON
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // Endpoint para login
    @PostMapping("/login")
    // valid activa las validaciones de LoginRequest
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Optional<User> user = userService.login(request.getEmail(), request.getPassword()); // autentica al usuario

        // si lo que ingreso es correcto
        if (user.isPresent()) {
            String token = jwtUtil.generateToken(user.get().getId()); // genera el token
            return ResponseEntity.ok(new LoginResponse(user.get(), token)); // devuelve el usuario y el token
        } else { // sí está mal, devuelve un 401 y no genera el token
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
}
