package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.RoutineRequest;
import com.tfg.proyecto.backend.entity.Routine;
import com.tfg.proyecto.backend.entity.User;
import com.tfg.proyecto.backend.repository.RoutineRepository;
import com.tfg.proyecto.backend.repository.UserRepository;
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

    public RoutineController(RoutineRepository routineRepository, UserRepository userRepository) {
        this.routineRepository = routineRepository;
        this.userRepository = userRepository;
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
                request.getHydrationIntervalHours()
        );

        // Guardar en la base de datos
        Routine saved = routineRepository.save(routine);

        return ResponseEntity.ok(saved);
    }

    // Obtener rutina por paciente
    @GetMapping("/routine/{patientId}")
    public ResponseEntity<?> obtenerRutina(@PathVariable Long patientId) {
        return routineRepository.findByPatientId(patientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
