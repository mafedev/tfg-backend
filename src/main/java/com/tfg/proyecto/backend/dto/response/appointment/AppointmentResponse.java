package com.tfg.proyecto.backend.dto.response.appointment;

import java.time.LocalDateTime;

public class AppointmentResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime dateTime;
    private String status;

    public AppointmentResponse(Long id, String title, String description, String location, LocalDateTime dateTime, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.status = status;
    }

    public Long getId() {
        return id;
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

    public String getStatus() {
        return status;
    }
}