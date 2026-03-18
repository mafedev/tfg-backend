package com.tfg.proyecto.backend.model.users;

import jakarta.persistence.*;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;

    @Enumerated(EnumType.STRING)
    private DiseaseStage diseaseStage;

    @Column(length = 1000)
    private String notes;

    // Es opcional, ya que puede que el paciente tenga una cuenta, o no
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public enum DiseaseStage {
        EARLY,
        MODERATE,
        ADVANCED
    }

    // -------------------- Constructores --------------------
    public Patient() {
    }

    public Patient(Long id, User user, DiseaseStage diseaseStage, String notes) {
        this.id = id;
        this.user = user;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
