package com.tfg.proyecto.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medications_schedules")
public class MedicationSchedules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    private String hour;
    private int dose;
    private String frequency; // podría ser enum

    // Constructores
    public MedicationSchedules() {
    }

    public MedicationSchedules(String frequency, int dose, String hour, Medication medication, User patient, Long id) {
        this.frequency = frequency;
        this.dose = dose;
        this.hour = hour;
        this.medication = medication;
        this.patient = patient;
        this.id = id;
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

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}