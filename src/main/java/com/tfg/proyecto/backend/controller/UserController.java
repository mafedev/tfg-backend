package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.response.PatientListDetailsResponse;
import com.tfg.proyecto.backend.model.User;
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
    private final JwtUtil jwtUtil;

    public UserController(UserRepository userRepository, UserService userService, RelationRepository relationRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.relationRepository = relationRepository;
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

            if (user.getRole().equals("CUIDADOR")) {
                boolean linked = relationRepository.existsByCaregiverIdAndPatientId(userId, id);

                if (!linked) return null;
                return userService.getPatientDetails(id);
            }
        }
        return null;
    }
}
