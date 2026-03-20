package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.RoutineRequest;
import com.tfg.proyecto.backend.model.users.Caregiver;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.routine.Routine;
import com.tfg.proyecto.backend.repository.CaregiverRepository;
import com.tfg.proyecto.backend.repository.PatientRepository;
import com.tfg.proyecto.backend.repository.RelationRepository;
import com.tfg.proyecto.backend.repository.RoutineRepository;
import com.tfg.proyecto.backend.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RoutineController {

    private final RoutineRepository routineRepository;
    private final PatientRepository patientRepository;
    private final RelationRepository relationRepository;
    private final CaregiverRepository caregiverRepository;
    private final JwtUtil jwtUtil;

    // Inyección de dependencias
    public RoutineController(RoutineRepository routineRepository, PatientRepository patientRepository, RelationRepository relationRepository, CaregiverRepository caregiverRepository, JwtUtil jwtUtil) {
        this.routineRepository = routineRepository;
        this.patientRepository = patientRepository;
        this.relationRepository = relationRepository;
        this.caregiverRepository = caregiverRepository;
        this.jwtUtil = jwtUtil;
    }

    // ------------- Crear una rutina -------------
    @PostMapping("/routine/{patientId}")
    public ResponseEntity<?> crearRutina(
            @PathVariable Long patientId,
            @RequestBody RoutineRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        Long userId = extractId(authHeader);

        Caregiver caregiver = caregiverRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Caregiver no encontrado"));

        Long caregiverId = caregiver.getId();

        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiverId, patientId);
        if (!allowed) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (patient.getUser() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El paciente no tiene usuario asignado");
        }

        if (routineRepository.findByPatientId(patientId).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El paciente ya tiene una rutina");
        }

        Routine routine = new Routine();
        routine.setPatient(patient);
        routine.setBreakfastTime(request.getBreakfastTime());
        routine.setLunchTime(request.getLunchTime());
        routine.setDinnerTime(request.getDinnerTime());
        routine.setBathTime(request.getBathTime());
        routine.setHydrationEnabled(request.isHydrationEnabled());
        routine.setHydrationReminderHours(request.getHydrationReminderHours());

        return ResponseEntity.ok(routineRepository.save(routine));
    }

    // ------------- Obtener rutina (paciente) -------------
    @GetMapping("/routine")
    public ResponseEntity<?> obtenerRutina(@RequestHeader("Authorization") String authHeader) {
        // Extrae el id del paciente desde el token para obtener su propia rutina
        Long patientId = extractId(authHeader);
        // Busca la rutina del paciente
        Routine routine = routineRepository.findByPatientId(patientId).orElse(null);

        if (routine != null) { // si existe, devuelve 200 con la rutina
            return ResponseEntity.ok(routine);
        } else { // si no existe, devuelve 404
            return ResponseEntity.notFound().build();
        }

    }

    // ------------- Obtener rutina de un paciente (cuidador) -------------
    @GetMapping("/routine/{patientId}")
    public ResponseEntity<?> obtenerRutinaCuidador(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {
        Long userId = extractId(authHeader);

        Caregiver caregiver = caregiverRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Caregiver no encontrado"));

        Long caregiverId = caregiver.getId();

        // Verifica que el cuidador tenga relación con el paciente
        boolean allowed = relationRepository
                .existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Busca la rutina y devuelve 200 si existe, 404 si no
        return routineRepository.findByPatientId(patientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------- Editar una rutina -------------
    @PutMapping("/routine/{patientId}")
    public ResponseEntity<?> editarRutina(@PathVariable Long patientId, @RequestBody RoutineRequest request, @RequestHeader("Authorization") String authHeader) {
        Long userId = extractId(authHeader);

        Caregiver caregiver = caregiverRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Caregiver no encontrado"));

        Long caregiverId = caregiver.getId();

        // Verifica que el cuidador tenga relación con el paciente
        boolean allowed = relationRepository
                .existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Obtiene la rutina existente
        Routine routine = routineRepository.findByPatientId(patientId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        // Actualiza los datos de la rutina con los nuevos
        routine.setBreakfastTime(request.getBreakfastTime());
        routine.setLunchTime(request.getLunchTime());
        routine.setDinnerTime(request.getDinnerTime());
        routine.setBathTime(request.getBathTime());
        routine.setHydrationEnabled(request.isHydrationEnabled());
        routine.setHydrationReminderHours(request.getHydrationReminderHours());

        // guarda la rutina y la devuelve con el código 200
        return ResponseEntity.ok(routineRepository.save(routine));
    }

    // ------------- Eliminar una rutina -------------
    @DeleteMapping("/routine/{patientId}")
    public ResponseEntity<?> eliminarRutina(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {
        Long userId = extractId(authHeader);

        Caregiver caregiver = caregiverRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Caregiver no encontrado"));

        Long caregiverId = caregiver.getId();

        // Verifica que el cuidador tenga relación con el paciente
        boolean allowed = relationRepository
                .existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Busca la rutina
        Routine routine = routineRepository.findByPatientId(patientId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        // si la encuentra la elimina
        routineRepository.delete(routine);

        // devuelve 200 si salió bien
        return ResponseEntity.ok().build();
    }


    // Método de extracción
    private Long extractId(String authHeader) {
        String token = authHeader.substring(7); // quitar "Bearer "
        return Long.parseLong(jwtUtil.extractId(token));
    }
}
