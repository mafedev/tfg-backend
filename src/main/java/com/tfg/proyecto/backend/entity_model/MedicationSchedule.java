package com.tfg.proyecto.backend.entity_model;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "medications_schedules")
public class MedicationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @Column(nullable = false)
    private LocalTime hour;

    @Column(nullable = false)
    private int dose;

    @Column(nullable = false)
    private int frequency;

    // Constructores
    public MedicationSchedule() {
    }

    public MedicationSchedule(User patient, Medication medication, LocalTime hour, int dose, int frequency) {
        this.patient = patient;
        this.medication = medication;
        this.hour = hour;
        this.dose = dose;
        this.frequency = frequency;
    }

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

    public LocalTime getHour() {
        return hour;
    }

    public void setHour(LocalTime hour) {
        this.hour = hour;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}