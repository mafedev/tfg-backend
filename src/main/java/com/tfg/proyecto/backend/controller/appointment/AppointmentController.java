package com.tfg.proyecto.backend.controller.appointment;

import com.tfg.proyecto.backend.dto.request.AppointmentRequest;
import com.tfg.proyecto.backend.dto.response.AppointmentResponse;
import com.tfg.proyecto.backend.model.appointment.Appointment;
import com.tfg.proyecto.backend.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Crear cita
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest request) {
        Appointment appointment = appointmentService.createAppointment(request);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        boolean deleted = appointmentService.deleteAppointment(id);
        if (!deleted) {
            return ResponseEntity.status(404).body("Cita no encontrada");
        }

        return ResponseEntity.ok("Cita eliminada");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody AppointmentRequest dto) {
        Appointment updated = appointmentService.updateAppointment(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam Appointment.AppointmentStatus status) {
        appointmentService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }
}