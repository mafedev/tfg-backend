package com.tfg.proyecto.backend.dto.request;

import java.time.LocalDateTime;

public class AppointmentRequest {

    private String title;
    private String description;
    private String location;
    private LocalDateTime dateTime;
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