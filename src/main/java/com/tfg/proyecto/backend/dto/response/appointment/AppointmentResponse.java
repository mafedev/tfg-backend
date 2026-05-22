package com.tfg.proyecto.backend.dto.response.appointment;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AppointmentResponse {
    // -------------------- Getters --------------------
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime dateTime;
    private String status;

    // -------------------- Constructor --------------------
    public AppointmentResponse(Long id, String title, String description, String location, LocalDateTime dateTime, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.status = status;
    }
}