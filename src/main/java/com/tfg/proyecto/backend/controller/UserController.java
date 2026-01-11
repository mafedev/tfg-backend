package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.SignupRequest;
import com.tfg.proyecto.backend.dto.response.PatientListDetailsResponse;
import com.tfg.proyecto.backend.entity_model.User;
import com.tfg.proyecto.backend.repository.RelationRepository;
import com.tfg.proyecto.backend.repository.UserRepository;
import com.tfg.proyecto.backend.security.JwtUtil;
import com.tfg.proyecto.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api") // Indica que todas las rutas dentro de esta clase empezarán por /api
@CrossOrigin(origins = "*") // Permite que el frontend pueda hacer peticiones desde otro dominio (sin esto, el navegador podría bloquear las peticiones por CORS)
public class UserController {
    // inyección de dependencias
    private final UserRepository userRepository;
    private final UserService userService;
    private final RelationRepository relationRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public UserController(UserRepository userRepository, UserService userService, RelationRepository relationRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.relationRepository = relationRepository;
    }

    // Este método responde a una petición GET, por ejemplo:
    // GET http://localhost:8080/api/users/1
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        // @PathVariable significa que el valor {id} de la URL se pasa al método como parámetro

        // Buscamos el usuario en la base de datos por su ID
        Optional<User> u = userRepository.findById(id);

        // Si se encuentra, lo devolvemos con código 200 OK
        // Si no, devolvemos un 404 Not Found
        return u.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Devuelve la info del paciente
    @GetMapping("/patients/{id}")
    public PatientListDetailsResponse getPatientDetails(@PathVariable Long id, @RequestHeader("Authorization") String authHeader){

        String token = authHeader.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractId(token));

        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();

            if(user.getRole().equals("CUIDADOR")){
                boolean linked = relationRepository.existsByCaregiverIdAndPatientId(userId, id);

                if(linked) return null;
                return userService.getPatientDetails(id);
            }
        } return null;
    }
}
