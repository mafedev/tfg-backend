package com.tfg.proyecto.backend.controller.routine;

import com.tfg.proyecto.backend.model.routine.RoutineTracking;
import com.tfg.proyecto.backend.service.routine.RoutineTrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routine-tracking")
public class RoutineTrackingController {

    private final RoutineTrackingService trackingService;

    public RoutineTrackingController(RoutineTrackingService trackingService) {
        this.trackingService = trackingService;
    }

    // Obtiene o crea la fila del seguimiento del día
    @GetMapping("/today/{patientId}")
    public ResponseEntity<?> getToday(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {
        RoutineTracking result = trackingService.getTracking(patientId, authHeader);

        if (result == null) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(result);
    }

    // Marca cualquier actividad como hecha (TRUE)
    @PutMapping("/{patientId}/{item}")
    public ResponseEntity<?> updateItem(@PathVariable Long patientId, @PathVariable String item, @RequestParam boolean done, @RequestHeader("Authorization") String authHeader) {
        RoutineTracking result = trackingService.updateItem(patientId, item, done, authHeader);

        if (result == null) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(result);
    }
}
