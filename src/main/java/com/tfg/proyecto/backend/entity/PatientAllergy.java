package com.tfg.proyecto.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "patients_allergies")
public class PatientAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "alergy_id", nullable = false)

    private String severity;

    // Constructores
    public PatientAllergy() {
    }

    public PatientAllergy(String severity, User patient, Long id) {
        this.severity = severity;
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
