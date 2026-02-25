package com.tfg.proyecto.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Es opcional, ya que puede que el paciente tenga una cuenta, o no
    @OneToOne
    @JoinColumn(name = "patient_id", unique = true)
    private User patient;

    @Enumerated(EnumType.STRING)
    private DiseaseStage diseaseStage;

    @Column(length = 1000)
    private String notes;

    public enum DiseaseStage {
        EARLY,
        MODERATE,
        ADVANCED
    }

    // -------------------- Constructores --------------------
    public Patient() {
    }

    public Patient(Long id, User patient, DiseaseStage diseaseStage, String notes) {
        this.id = id;
        this.patient = patient;
        this.diseaseStage = diseaseStage;
        this.notes = notes;
    }

    // -------------------- Getters y Setters --------------------
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

    public DiseaseStage getDiseaseStage() {
        return diseaseStage;
    }

    public void setDiseaseStage(DiseaseStage diseaseStage) {
        this.diseaseStage = diseaseStage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
