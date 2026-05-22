package com.tfg.proyecto.backend.dto.response.home;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class HomeCalendarResponse {
    private LocalDate date;

    // -------------------- Constructor --------------------
    public HomeCalendarResponse(LocalDate date) {
        this.date = date;
    }
}