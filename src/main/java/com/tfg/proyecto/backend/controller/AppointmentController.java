package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.AppointmentRequest;
import com.tfg.proyecto.backend.model.appointment.Appointment;
import com.tfg.proyecto.backend.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}