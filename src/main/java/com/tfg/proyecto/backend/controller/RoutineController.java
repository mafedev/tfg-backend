package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.RoutineRequest;
import com.tfg.proyecto.backend.entity_model.Routine;
import com.tfg.proyecto.backend.entity_model.User;
import com.tfg.proyecto.backend.repository.RoutineRepository;
import com.tfg.proyecto.backend.repository.UserRepository;
import com.tfg.proyecto.backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RoutineController {

    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public RoutineController(RoutineRepository routineRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.routineRepository = routineRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // Crear una rutina
    @PostMapping("/routine")
    public ResponseEntity<?> crearRutina(@RequestBody RoutineRequest request) {
        // Buscar al paciente
        Optional<User> patientOpt = userRepository.findById(request.getPatientId());
        if (patientOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Paciente no encontrado con ID: " + request.getPatientId());
        }

        User patient = patientOpt.get();

        // Convertir strings de hora a LocalTime
        Routine routine = new Routine(
                patient,
                LocalTime.parse(request.getBreakfastTime()),
                LocalTime.parse(request.getLunchTime()),
                LocalTime.parse(request.getDinnerTime()),
                LocalTime.parse(request.getBathTime()),
                request.getEnableHydration(),
                request.getWaterReminder()
        );

        // Guardar en la base de datos
        Routine saved = routineRepository.save(routine);

        return ResponseEntity.ok(saved);
    }

    // Obtener rutina por paciente
    @GetMapping("/routine")
    public ResponseEntity<?> obtenerRutina(@RequestHeader("Authorization") String authHeader) {
        Long patientId = extractId(authHeader);
        Routine routine = routineRepository.findByPatientId(patientId).orElse(null);

        if (routine != null) {
            return ResponseEntity.ok(routine);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    // Método de extracción
    private Long extractId(String authHeader) {
        String token = authHeader.substring(7); // quitar "Bearer "
        return Long.parseLong(jwtUtil.extractId(token));
    }
}
