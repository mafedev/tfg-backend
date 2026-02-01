package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.RoutineRequest;
import com.tfg.proyecto.backend.entity_model.Routine;
import com.tfg.proyecto.backend.entity_model.User;
import com.tfg.proyecto.backend.repository.RelationRepository;
import com.tfg.proyecto.backend.repository.RoutineRepository;
import com.tfg.proyecto.backend.repository.UserRepository;
import com.tfg.proyecto.backend.security.JwtUtil;
import org.springframework.http.HttpStatus;
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
    private final RelationRepository relationRepository;
    private final JwtUtil jwtUtil;

    public RoutineController(RoutineRepository routineRepository, UserRepository userRepository, RelationRepository relationRepository, JwtUtil jwtUtil) {
        this.routineRepository = routineRepository;
        this.userRepository = userRepository;
        this.relationRepository = relationRepository;
        this.jwtUtil = jwtUtil;
    }

    // Crear una rutina
    @PostMapping("/routine/{patientId}")
    public ResponseEntity<?> crearRutina(@PathVariable Long patientId,@RequestBody RoutineRequest request,@RequestHeader("Authorization") String authHeader) {
        Long caregiverId = extractId(authHeader);

        boolean allowed = relationRepository
                .existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (routineRepository.findByPatientId(patientId).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El paciente ya tiene una rutina");
        }

        Routine routine = new Routine(
                patient,
                LocalTime.parse(request.getBreakfastTime()),
                LocalTime.parse(request.getLunchTime()),
                LocalTime.parse(request.getDinnerTime()),
                LocalTime.parse(request.getBathTime()),
                request.getEnableHydration(),
                request.getWaterReminder()
        );

        return ResponseEntity.ok(routineRepository.save(routine));
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

    @GetMapping("/routine/{patientId}")
    public ResponseEntity<?> obtenerRutinaCuidador(@PathVariable Long patientId,@RequestHeader("Authorization") String authHeader) {
        Long caregiverId = extractId(authHeader);

        boolean allowed = relationRepository
                .existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return routineRepository.findByPatientId(patientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/routine/{patientId}")
    public ResponseEntity<?> editarRutina(@PathVariable Long patientId,@RequestBody RoutineRequest request,@RequestHeader("Authorization") String authHeader) {
        Long caregiverId = extractId(authHeader);

        boolean allowed = relationRepository
                .existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Routine routine = routineRepository.findByPatientId(patientId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        routine.setBreakfastTime(LocalTime.parse(request.getBreakfastTime()));
        routine.setLunchTime(LocalTime.parse(request.getLunchTime()));
        routine.setDinnerTime(LocalTime.parse(request.getDinnerTime()));
        routine.setBathTime(LocalTime.parse(request.getBathTime()));
        routine.setEnableHydration(request.getEnableHydration());
        routine.setWaterReminder(request.getWaterReminder());

        return ResponseEntity.ok(routineRepository.save(routine));
    }

    @DeleteMapping("/routine/{patientId}")
    public ResponseEntity<?> eliminarRutina(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {
        Long caregiverId = extractId(authHeader);

        boolean allowed = relationRepository
                .existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Routine routine = routineRepository.findByPatientId(patientId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        routineRepository.delete(routine);
        return ResponseEntity.ok().build();
    }


    // Método de extracción
    private Long extractId(String authHeader) {
        String token = authHeader.substring(7); // quitar "Bearer "
        return Long.parseLong(jwtUtil.extractId(token));
    }
}
