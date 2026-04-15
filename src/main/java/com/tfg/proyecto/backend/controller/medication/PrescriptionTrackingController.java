package com.tfg.proyecto.backend.controller.medication;

import com.tfg.proyecto.backend.dto.request.medication.PrescriptionTrackingRequest;
import com.tfg.proyecto.backend.service.medication.PrescriptionTrackingService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PrescriptionTrackingController {

    private final PrescriptionTrackingService service;

    public PrescriptionTrackingController(PrescriptionTrackingService service) {
        this.service = service;
    }

    // -------------------- Obtiene el seguimiento por fecha --------------------
    // recibe el id del paciente y una fecha, devuelve todas las tomas de ese día
    @GetMapping("/{patientId}/tracking")
    public List<PrescriptionTrackingRequest> getPrescriptionTracking(@PathVariable Long patientId, @RequestParam String date) {
        LocalDate parsedDate = LocalDate.parse(date); // convierte el string a LocalDate
        return service.getPrescriptionTracking(patientId, parsedDate); // llama al service
    }

    // -------------------- Toggle del seguimiento --------------------
    // Cambia entre true y false el estado de la toma del medicamento
    @PutMapping("/tracking/toggle")
    public void toggleTracking(@RequestParam Long prescriptionId, @RequestParam String scheduledTime) {
        LocalDateTime dateTime = LocalDateTime.parse(scheduledTime); // convierte a LocalDateTime
        service.toggleTracking(prescriptionId, dateTime); // llama al service
    }
}
