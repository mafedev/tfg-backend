package com.tfg.proyecto.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "allergies")
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @Column(nullable = false)
    private String name; // alimento o sustancia

    private String severity; // leve, moderada, grave

    // Constructores
    public Allergy() {
    }

    public Allergy(Long id, User patient, String name, String severity) {
        this.id = id;
        this.patient = patient;
        this.name = name;
        this.severity = severity;
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
