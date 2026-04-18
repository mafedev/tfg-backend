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

    // -------------------- Crear cita --------------------
    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.createAppointment(request);
        return ResponseEntity.status(201).body(response);
    }

    // -------------------- Obtener citas por paciente --------------------
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getByPatient(@PathVariable Long patientId) {
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(appointments);
    }

    // -------------------- Eliminar cita --------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        boolean deleted = appointmentService.deleteAppointment(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    // -------------------- Actualizar cita completa --------------------
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> updateAppointment(@PathVariable Long id, @RequestBody AppointmentRequest request) {
        AppointmentResponse updated = appointmentService.updateAppointment(id, request);
        return ResponseEntity.ok(updated);
    }

    // -------------------- Actualizar solo el estado --------------------
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam Appointment.AppointmentStatus status) {
        boolean updated = appointmentService.updateStatus(id, status);

        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}