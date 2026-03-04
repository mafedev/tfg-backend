package com.tfg.proyecto.backend.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class RoutineRequest {

    @NotNull
    private Long patientId;

    private LocalTime breakfastTime;
    private LocalTime lunchTime;
    private LocalTime dinnerTime;
    private LocalTime bathTime;

    private boolean hydrationEnabled;
    private Integer hydrationReminderHours;

    // -------------------- Constructor --------------------
    public RoutineRequest() {
    }

    // -------------------- Getters y Setters --------------------
    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public LocalTime getBreakfastTime() {
        return breakfastTime;
    }

    public void setBreakfastTime(LocalTime breakfastTime) {
        this.breakfastTime = breakfastTime;
    }

    public LocalTime getLunchTime() {
        return lunchTime;
    }

    public void setLunchTime(LocalTime lunchTime) {
        this.lunchTime = lunchTime;
    }

    public LocalTime getDinnerTime() {
        return dinnerTime;
    }

    public void setDinnerTime(LocalTime dinnerTime) {
        this.dinnerTime = dinnerTime;
    }

    public LocalTime getBathTime() {
        return bathTime;
    }

    public void setBathTime(LocalTime bathTime) {
        this.bathTime = bathTime;
    }

    public boolean isHydrationEnabled() {
        return hydrationEnabled;
    }

    public void setHydrationEnabled(boolean hydrationEnabled) {
        this.hydrationEnabled = hydrationEnabled;
    }

    public Integer getHydrationReminderHours() {
        return hydrationReminderHours;
    }

    public void setHydrationReminderHours(Integer hydrationReminderHours) {
        this.hydrationReminderHours = hydrationReminderHours;
    }
}
