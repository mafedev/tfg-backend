package com.tfg.proyecto.backend.dto.response.home;

import java.time.LocalDate;

public class HomeCalendarResponse {
    private LocalDate date;

    public HomeCalendarResponse() {
    }

    public HomeCalendarResponse(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}