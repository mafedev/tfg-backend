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
    public ResponseEntity<RoutineResponse> createRoutine(@PathVariable Long patientId) {
        return ResponseEntity.ok(routineService.createRoutine(patientId));
    }

    // ------------- Obtener rutina de un paciente (cuidador) -------------
    @GetMapping("/{patientId}")
    public ResponseEntity<RoutineResponse> getRoutineByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(routineService.getRoutineAsCaregiver(patientId));
    }

    // ------------- Borrar rutina -------------
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long patientId) {
        routineService.deleteRoutine(patientId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<RoutineResponse> getMyRoutine() {
        return ResponseEntity.ok(routineService.getRoutineAsPatient());
    }
}
