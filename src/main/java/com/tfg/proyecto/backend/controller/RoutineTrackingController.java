package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.repository.RelationRepository;
import com.tfg.proyecto.backend.security.JwtUtil;
import com.tfg.proyecto.backend.service.RoutineTrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routine-tracking")
public class RoutineTrackingController {

    private final RoutineTrackingService trackingService;
    private final RelationRepository relationRepository;
    private final JwtUtil jwtUtil;

    public RoutineTrackingController(RoutineTrackingService trackingService, RelationRepository relationRepository, JwtUtil jwtUtil) {
        this.trackingService = trackingService;
        this.relationRepository = relationRepository;
        this.jwtUtil = jwtUtil;
    }

    // Obtiene o crea la fila del seguimiento del día
    @GetMapping("/today/{patientId}")
    public ResponseEntity<?> getToday(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {

        Long userId = Long.parseLong(jwtUtil.extractId(authHeader.substring(7))); // extrae el id

        // comprueba que tengan una relación
        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(userId, patientId);

        // si no está autorizado, devuelve 403
        if (!allowed){
            return ResponseEntity.status(403).build();
        }

        // si lo está, entonces devuelve el día y
        return ResponseEntity.ok(trackingService.getTracking(patientId));
    }

    // Marca el desayuno como hecho (TRUE)
    @PutMapping("/{patientId}/breakfast")
    public ResponseEntity<?> markBreakfast(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {
        // obtiene el id
        Long caregiverId = Long.parseLong(jwtUtil.extractId(authHeader.substring(7)));

        // Verifica que tengan una relación
        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return ResponseEntity.status(403).build(); // si no tienen una relación, devuelve 403
        }
        // si tienen relación marca la casilla como TRUE
        return ResponseEntity.ok(trackingService.markBreakfast(patientId));
    }

    // ------------------- ALMUERZO -------------------
    @PutMapping("/{patientId}/lunch")
    public ResponseEntity<?> markLunch(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {

        Long caregiverId = Long.parseLong(jwtUtil.extractId(authHeader.substring(7)));

        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(trackingService.markLunch(patientId));
    }

    // ------------------- CENA -------------------
    @PutMapping("/{patientId}/dinner")
    public ResponseEntity<?> markDinner(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {
        Long caregiverId = Long.parseLong(jwtUtil.extractId(authHeader.substring(7)));

        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(trackingService.markDinner(patientId));
    }

    // ------------------- BAÑO -------------------
    @PutMapping("/{patientId}/bath")
    public ResponseEntity<?> markBath(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {
        Long caregiverId = Long.parseLong(jwtUtil.extractId(authHeader.substring(7)));

        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiverId, patientId);
        if (!allowed) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(trackingService.markBath(patientId));
    }

    // ------------------- HIDRATACIÓN -------------------
    @PutMapping("/{patientId}/hydration")
    public ResponseEntity<?> markHydration(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {
        Long caregiverId = Long.parseLong(jwtUtil.extractId(authHeader.substring(7)));
        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiverId, patientId);
        if (!allowed) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(trackingService.markHydration(patientId));
    }
}