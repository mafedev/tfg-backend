package com.tfg.proyecto.backend.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "routines")
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con el paciente (usuario)
    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    // Horarios de comida
    private LocalTime breakfastTime;
    private LocalTime lunchTime;
    private LocalTime dinnerTime;

    // Baño y agua
    private LocalTime bathTime;
    private Boolean enableHydration;
    private Integer waterReminder;

    // Constructores
    public Routine() {}

    public Routine(User patient, LocalTime breakfastTime, LocalTime lunchTime, LocalTime dinnerTime,
                   LocalTime bathTime, Boolean enableHydration, Integer waterReminder) {
        this.patient = patient;
        this.breakfastTime = breakfastTime;
        this.lunchTime = lunchTime;
        this.dinnerTime = dinnerTime;
        this.bathTime = bathTime;
        this.enableHydration = enableHydration;
        this.waterReminder = waterReminder;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getPatient() { return patient; }
    public void setPatient(User patient) { this.patient = patient; }

    public LocalTime getBreakfastTime() { return breakfastTime; }
    public void setBreakfastTime(LocalTime breakfastTime) { this.breakfastTime = breakfastTime; }

    public LocalTime getLunchTime() { return lunchTime; }
    public void setLunchTime(LocalTime lunchTime) { this.lunchTime = lunchTime; }

    public LocalTime getDinnerTime() { return dinnerTime; }
    public void setDinnerTime(LocalTime dinnerTime) { this.dinnerTime = dinnerTime; }

    public LocalTime getBathTime() { return bathTime; }
    public void setBathTime(LocalTime bathTime) { this.bathTime = bathTime; }

    public Boolean getEnableHydration() { return enableHydration; }
    public void setEnableHydration(Boolean enableHydration) { this.enableHydration = enableHydration; }

    public Integer getWaterReminder() { return waterReminder; }
    public void setWaterReminder(Integer waterReminder) { this.waterReminder = waterReminder; }
}
