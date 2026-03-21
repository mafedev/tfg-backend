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

    public RoutineController(RoutineRepository routineRepository,
                             PatientRepository patientRepository,
                             RelationRepository relationRepository,
                             CaregiverRepository caregiverRepository,
                             JwtUtil jwtUtil) {
        this.routineRepository = routineRepository;
        this.patientRepository = patientRepository;
        this.relationRepository = relationRepository;
        this.caregiverRepository = caregiverRepository;
        this.jwtUtil = jwtUtil;
    }

    // ---------------- Crear rutina ----------------
    @PostMapping("/routine/{patientId}")
    public ResponseEntity<?> crearRutina(@PathVariable Long patientId,
                                         @RequestBody RoutineRequest request,
                                         @RequestHeader("Authorization") String authHeader) {
        try {
            Long userId = extractId(authHeader);
            System.out.println("[DEBUG] Token convertido a userId: " + userId);

            Caregiver caregiver = caregiverRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Caregiver no encontrado"));
            System.out.println("[DEBUG] Caregiver encontrado: " + caregiver.getId() + ", " + caregiver.getName());

            boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiver.getId(), patientId);
            System.out.println("[DEBUG] Relación caregiver-patient permitida? " + allowed);
            if (!allowed) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            System.out.println("[DEBUG] Paciente encontrado: " + patient.getId() + ", " + patient.getName());

            if (routineRepository.findByPatientId(patient.getId()).isPresent()) {
                System.out.println("[DEBUG] Paciente ya tiene rutina");
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

            Routine saved = routineRepository.save(routine);
            System.out.println("[DEBUG] Rutina creada con id: " + saved.getId());

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            System.out.println("[ERROR] Crear rutina: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creando rutina: " + e.getMessage());
        }
    }

    // ---------------- Obtener rutina (paciente) ----------------
    @GetMapping("/routine")
    public ResponseEntity<?> obtenerRutina(@RequestHeader("Authorization") String authHeader) {
        try {
            Long userId = extractId(authHeader);
            System.out.println("[DEBUG] Token convertido a userId (obtenerRutina): " + userId);

            Patient patient = patientRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            System.out.println("[DEBUG] Paciente encontrado: " + patient.getId() + ", " + patient.getName());

            return routineRepository.findByPatientId(patient.getId())
                    .map(r -> {
                        System.out.println("[DEBUG] Rutina encontrada: " + r.getId());
                        return ResponseEntity.ok(r);
                    })
                    .orElseGet(() -> {
                        System.out.println("[DEBUG] No existe rutina para el paciente");
                        return ResponseEntity.notFound().build();
                    });

        } catch (Exception e) {
            System.out.println("[ERROR] Obtener rutina: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error obteniendo rutina: " + e.getMessage());
        }
    }

    // ---------------- Obtener rutina de paciente (cuidador) ----------------
    @GetMapping("/routine/{patientId}")
    public ResponseEntity<?> obtenerRutinaCuidador(@PathVariable Long patientId,
                                                   @RequestHeader("Authorization") String authHeader) {
        try {
            Long userId = extractId(authHeader);
            System.out.println("[DEBUG] Token convertido a userId (cuidador): " + userId);

            Caregiver caregiver = caregiverRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Caregiver no encontrado"));
            System.out.println("[DEBUG] Caregiver encontrado: " + caregiver.getId() + ", " + caregiver.getName());

            boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiver.getId(), patientId);
            System.out.println("[DEBUG] Relación caregiver-patient permitida? " + allowed);
            if (!allowed) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

            return routineRepository.findByPatientId(patientId)
                    .map(r -> {
                        System.out.println("[DEBUG] Rutina encontrada: " + r.getId());
                        return ResponseEntity.ok(r);
                    })
                    .orElseGet(() -> {
                        System.out.println("[DEBUG] No existe rutina para el paciente");
                        return ResponseEntity.notFound().build();
                    });

        } catch (Exception e) {
            System.out.println("[ERROR] Obtener rutina cuidador: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error obteniendo rutina cuidador: " + e.getMessage());
        }
    }

    // ---------------- Editar rutina ----------------
    @PutMapping("/routine/{patientId}")
    public ResponseEntity<?> editarRutina(@PathVariable Long patientId,
                                          @RequestBody RoutineRequest request,
                                          @RequestHeader("Authorization") String authHeader) {
        try {
            Long userId = extractId(authHeader);
            System.out.println("[DEBUG] Token convertido a userId (editar): " + userId);

            Caregiver caregiver = caregiverRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Caregiver no encontrado"));

            boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiver.getId(), patientId);
            if (!allowed) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

            Routine routine = routineRepository.findByPatientId(patientId)
                    .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

            routine.setBreakfastTime(request.getBreakfastTime());
            routine.setLunchTime(request.getLunchTime());
            routine.setDinnerTime(request.getDinnerTime());
            routine.setBathTime(request.getBathTime());
            routine.setHydrationEnabled(request.isHydrationEnabled());
            routine.setHydrationReminderHours(request.getHydrationReminderHours());

            Routine saved = routineRepository.save(routine);
            System.out.println("[DEBUG] Rutina editada con id: " + saved.getId());

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            System.out.println("[ERROR] Editar rutina: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error editando rutina: " + e.getMessage());
        }
    }

    // ---------------- Eliminar rutina ----------------
    @DeleteMapping("/routine/{patientId}")
    public ResponseEntity<?> eliminarRutina(@PathVariable Long patientId,
                                            @RequestHeader("Authorization") String authHeader) {
        try {
            Long userId = extractId(authHeader);
            System.out.println("[DEBUG] Token convertido a userId (delete): " + userId);

            Caregiver caregiver = caregiverRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Caregiver no encontrado"));

            boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiver.getId(), patientId);
            if (!allowed) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

            Routine routine = routineRepository.findByPatientId(patientId)
                    .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

            routineRepository.delete(routine);
            System.out.println("[DEBUG] Rutina eliminada con id: " + routine.getId());

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            System.out.println("[ERROR] Eliminar rutina: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error eliminando rutina: " + e.getMessage());
        }
    }

    // ---------------- Helper ----------------
    private Long extractId(String authHeader) {
        String token = authHeader.substring(7);
        System.out.println("[DEBUG] Extraer token: " + token);
        Long id = Long.parseLong(jwtUtil.extractId(token));
        System.out.println("[DEBUG] Id extraído del token: " + id);
        return id;
    }
}