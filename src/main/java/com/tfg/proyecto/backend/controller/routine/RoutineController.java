package com.tfg.proyecto.backend.controller.routine;

import com.tfg.proyecto.backend.dto.response.routine.RoutineResponse;
import com.tfg.proyecto.backend.service.routine.RoutineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routine")
public class RoutineController {

    private final RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    // ------------- Crear una rutina -------------
    @PostMapping("/{patientId}")
    public ResponseEntity<RoutineResponse> createRoutine(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {
        RoutineResponse response = routineService.createRoutine(patientId, authHeader);
        return ResponseEntity.ok(response);
    }

    // ------------- Obtener rutina de un paciente (cuidador) -------------
    @GetMapping("/{patientId}")
    public ResponseEntity<RoutineResponse> getRoutineByPatient(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {
        RoutineResponse response = routineService.getRoutineAsCaregiver(patientId, authHeader);
        return ResponseEntity.ok(response);
    }

    // ------------- Borrar rutina -------------
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long patientId, @RequestHeader("Authorization") String authHeader) {
        routineService.deleteRoutine(patientId, authHeader);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<RoutineResponse> getMyRoutine(@RequestHeader("Authorization") String authHeader) {
        RoutineResponse response = routineService.getRoutineAsPatient(authHeader);
        return ResponseEntity.ok(response);
    }
}
