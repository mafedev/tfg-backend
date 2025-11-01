package com.tfg.proyecto.backend.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con el paciente (User)
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String dose;

    private String frequency; // cada cuánto

    private LocalTime time; // hora del medicamento

    // Constructores

    public Medication() {
    }

    public Medication(Long id, User patient, String name, String dose, String frequency, LocalTime time) {
        this.id = id;
        this.patient = patient;
        this.name = name;
        this.dose = dose;
        this.frequency = frequency;
        this.time = time;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
