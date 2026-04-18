package com.tfg.proyecto.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class AppointmentRequest {
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede superar los 100 caracteres")
    private String title;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String description;

    @Size(max = 200, message = "La ubicación no puede superar los 200 caracteres")
    private String location;

    @NotNull(message = "La fecha y hora son obligatorias")
    private LocalDateTime dateTime;

    @NotNull(message = "El paciente es obligatorio")
    private Long patientId;

    public AppointmentRequest() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Long getPatientId() {
        return patientId;
    }
}