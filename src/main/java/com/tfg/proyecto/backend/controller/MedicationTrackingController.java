package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.MedicationTrackingRequest;
import com.tfg.proyecto.backend.service.MedicationTrackingService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class MedicationTrackingController {

    private final MedicationTrackingService service;

    public MedicationTrackingController(MedicationTrackingService service) {
        this.service = service;
    }

    // Obtiene el seguimiento del día
    @GetMapping("/{patientId}/tracking/today")
    public List<MedicationTrackingRequest> getTodayTracking(@PathVariable Long patientId) {
        return service.getTodayTracking(patientId); // obtiene el tracking de un paciente en específico
    }

    // Cambia entre true y false el estado de la toma del medicamento
    @PutMapping("/tracking/toggle")
    // recibe el id de la prescripción y la hora
    public void toggleTracking(@RequestParam Long prescriptionId, @RequestParam String scheduledTime) {
        service.toggleTracking(prescriptionId, LocalDateTime.parse(scheduledTime)); // le pasa la hora como localdatetime
    }
}
