package com.tfg.proyecto.backend.controller.medication;

import com.tfg.proyecto.backend.dto.request.medication.PrescriptionTrackingResponse;
import com.tfg.proyecto.backend.dto.request.medication.ToggleTrackingRequest;
import com.tfg.proyecto.backend.model.medication.PrescriptionTracking;
import com.tfg.proyecto.backend.security.JwtUtil;
import com.tfg.proyecto.backend.service.medication.PrescriptionTrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prescription-tracking")
public class PrescriptionTrackingController {

    private final PrescriptionTrackingService service;
    private final JwtUtil jwtUtil;

    public PrescriptionTrackingController(PrescriptionTrackingService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    // -------------------- Obtiene el seguimiento por fecha --------------------
    // recibe el id del paciente y una fecha, devuelve todas las tomas de ese día
    @GetMapping
    public List<PrescriptionTrackingResponse> getPrescriptionTracking(@RequestHeader("Authorization") String header, @RequestParam String date) {
        // se obtiene el token
        String token = header.substring(7);
        // se extrae el id del usuario
        Long userId = Long.parseLong(jwtUtil.extractId(token));
        LocalDate parsedDate = LocalDate.parse(date); // se pasa la fecha a localdate
        return service.getPrescriptionTracking(userId, parsedDate); // llama al service
    }

    // -------------------- Toggle del seguimiento --------------------
    // Cambia entre true y false el estado de la toma del medicamento
    @PutMapping("/toggle")
    public ResponseEntity<?> toggle(@RequestBody ToggleTrackingRequest request) {
        PrescriptionTracking tracking = service.toggleTracking(request.getPrescriptionId(), request.getScheduledTime());
        if (tracking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
