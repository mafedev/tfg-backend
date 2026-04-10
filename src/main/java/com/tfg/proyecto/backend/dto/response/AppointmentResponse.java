package com.tfg.proyecto.backend.dto.response;

import java.time.LocalDateTime;

public class AppointmentResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime dateTime;

    public AppointmentResponse(Long id, String title, String description, String location, LocalDateTime dateTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
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
}