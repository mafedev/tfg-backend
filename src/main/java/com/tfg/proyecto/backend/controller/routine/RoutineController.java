package com.tfg.proyecto.backend.controller.routine;

import com.tfg.proyecto.backend.dto.request.routine.RoutineRequest;
import com.tfg.proyecto.backend.model.routine.Routine;
import com.tfg.proyecto.backend.service.routine.RoutineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RoutineController {

    private final RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    // ------------- Crear una rutina -------------
    @PostMapping("/routine/{patientId}")
    public ResponseEntity<?> createRoutine(@PathVariable Long patientId, @RequestBody RoutineRequest request, @RequestHeader("Authorization") String authHeader) {
        Routine routine = routineService.createRoutine(patientId, request, authHeader);

        if (routine == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(routine);
    }

    // ------------- Obtener rutina (paciente) -------------
    @GetMapping("/routine")
    public ResponseEntity<?> getOwnRoutine(@RequestHeader("Authorization") String authHeader) {
        Routine routine = routineService.getOwnRoutine(authHeader);

        if (routine == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(routine);
    }

    // ------------- Obtener rutina de un paciente (cuidador) -------------
    @GetMapping("/routine/{patientId}")
    public ResponseEntity<?> getRoutineAsCaregiver(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {
        Routine routine = routineService.getRoutineAsCaregiver(patientId, authHeader);

        if (routine == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(routine);
    }

    // ------------- Editar una rutina -------------
    @PutMapping("/routine/{patientId}")
    public ResponseEntity<?> update(@PathVariable Long patientId, @RequestBody RoutineRequest request, @RequestHeader("Authorization") String authHeader) {
        Routine routine = routineService.updateRoutine(patientId, request, authHeader);

        if (routine == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(routine);
    }

    // ------------- Eliminar una rutina -------------
    @DeleteMapping("/routine/{patientId}")
    public ResponseEntity<?> delete(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {
        boolean deleted = routineService.deleteRoutine(patientId, authHeader);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }
}
