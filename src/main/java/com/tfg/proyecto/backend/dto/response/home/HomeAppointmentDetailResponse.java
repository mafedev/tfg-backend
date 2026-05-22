package com.tfg.proyecto.backend.dto.response.home;

import lombok.Getter;

@Getter
public class HomeAppointmentDetailResponse {
    // -------------------- Getters --------------------
    private String title;
    private String patientName;
    private String location;
    private String dateTime;

    // -------------------- Constructor --------------------
    public HomeAppointmentDetailResponse(String title, String patientName, String location, String dateTime) {
        this.title = title;
        this.patientName = patientName;
        this.location = location;
        this.dateTime = dateTime;
    }
}