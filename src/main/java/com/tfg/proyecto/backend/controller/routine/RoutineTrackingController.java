package com.tfg.proyecto.backend.controller.routine;

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
        // obtiene el id
        Long userId = Long.parseLong(jwtUtil.extractId(authHeader.substring(7)));
        // comprueba que tengan una relación
        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(userId, patientId);

        // si no está autorizado, devuelve 403
        if (!allowed) {
            return ResponseEntity.status(403).build();
        }

        // si lo está, entonces devuelve el día y
        return ResponseEntity.ok(trackingService.getTracking(patientId));
    }

    // Marca cualquier actividad como hecha (TRUE)
    @PutMapping("/{patientId}/{item}")
    public ResponseEntity<?> updateItem(@PathVariable Long patientId, @PathVariable String item, @RequestParam boolean done, @RequestHeader("Authorization") String authHeader) {
        // obtiene el id
        Long caregiverId = Long.parseLong(jwtUtil.extractId(authHeader.substring(7)));

        // Verifica que tengan una relación
        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return ResponseEntity.status(403).build();
        }
        // si tienen relación marca la casilla como TRUE
        return ResponseEntity.ok(trackingService.updateItem(patientId, item, done));
    }
}