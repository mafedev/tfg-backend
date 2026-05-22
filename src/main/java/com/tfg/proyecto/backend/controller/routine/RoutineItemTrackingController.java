package com.tfg.proyecto.backend.controller.routine;

import com.tfg.proyecto.backend.dto.response.routine.RoutineItemTrackingResponse;
import com.tfg.proyecto.backend.model.routine.RoutineItemTracking;
import com.tfg.proyecto.backend.service.routine.RoutineItemTrackingService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/routine/tracking")
public class RoutineItemTrackingController {

    private final RoutineItemTrackingService trackingService;

    public RoutineItemTrackingController(RoutineItemTrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping("/{patientId}")
    public List<RoutineItemTrackingResponse> getByPatientAndDate(@PathVariable Long patientId, @RequestParam String date) {
        LocalDate parsedDate = LocalDate.parse(date);

        if (parsedDate.equals(LocalDate.now())) {
            trackingService.generateDailyTracking(patientId, parsedDate);
        }

        return trackingService.getTrackingByPatientAndDate(patientId, parsedDate).stream().map(RoutineItemTrackingResponse::new).toList();
    }

    @PutMapping("/{trackingId}")
    public RoutineItemTracking updateStatus(@PathVariable Long trackingId, @RequestParam String status) {
        return trackingService.updateStatus(trackingId, RoutineItemTracking.Status.valueOf(status.toUpperCase()));
    }

    @GetMapping("/me")
    public List<RoutineItemTrackingResponse> getMyTracking(@RequestParam String date) {
        return trackingService.getTrackingForPatient(date);
    }
}
