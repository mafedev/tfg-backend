package com.tfg.proyecto.backend.model;

import jakarta.persistence.*;

@Entity
public class MedicationTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    private String schedule_time;
    private String taken_time;
    private Boolean status; // tomada o pendiente

    // Constructores
    public MedicationTracking() {
    }

    public MedicationTracking(Long id, Medication medication, String schedule_time, String taken_time, Boolean status) {
        this.id = id;
        this.medication = medication;
        this.schedule_time = schedule_time;
        this.taken_time = taken_time;
        this.status = status;
    }

    // Getters y Setters
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTaken_time() {
        return taken_time;
    }

    public void setTaken_time(String taken_time) {
        this.taken_time = taken_time;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
