package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.MedicationTrackingRequest;
import com.tfg.proyecto.backend.service.MedicationTrackingService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class MedicationTrackingController {

    private final MedicationTrackingService service;

    public MedicationTrackingController(MedicationTrackingService service) {
        this.service = service;
    }

    // -------------------- Obtiene el seguimiento por fecha --------------------
    // recibe el id del paciente y una fecha, devuelve todas las tomas de ese día
    @GetMapping("/{patientId}/tracking")
    public List<MedicationTrackingRequest> getMedicationTracking(@PathVariable Long patientId, @RequestParam String date) {
        LocalDate parsedDate = LocalDate.parse(date); // convierte el string a LocalDate
        return service.getMedicationTracking(patientId, parsedDate); // llama al service
    }

    // -------------------- Toggle del seguimiento --------------------
    // Cambia entre true y false el estado de la toma del medicamento
    @PutMapping("/tracking/toggle")
    public void toggleTracking(@RequestParam Long prescriptionId, @RequestParam String scheduledTime) {
        LocalDateTime dateTime = LocalDateTime.parse(scheduledTime); // convierte a LocalDateTime
        service.toggleTracking(prescriptionId, dateTime); // llama al service
    }
}
