package com.tfg.proyecto.backend.model.routine;

import com.tfg.proyecto.backend.model.Patient;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "routines")
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Paciente relacionado
    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    // Horarios de comida
    private LocalTime breakfastTime;
    private LocalTime lunchTime;
    private LocalTime dinnerTime;

    // Baño y agua
    private LocalTime bathTime;
    private boolean hydrationEnabled;
    private Integer hydrationReminderHours;

    // -------------------- Constructores --------------------
    public Routine() {
    }

    public Routine(Integer hydrationReminderHours, boolean hydrationEnabled, LocalTime bathTime, LocalTime dinnerTime, LocalTime lunchTime, LocalTime breakfastTime, Patient patient, Long id) {
        this.hydrationReminderHours = hydrationReminderHours;
        this.hydrationEnabled = hydrationEnabled;
        this.bathTime = bathTime;
        this.dinnerTime = dinnerTime;
        this.lunchTime = lunchTime;
        this.breakfastTime = breakfastTime;
        this.patient = patient;
        this.id = id;
    }

    // -------------------- Getters y Setters --------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
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
