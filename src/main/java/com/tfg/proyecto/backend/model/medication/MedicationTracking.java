package com.tfg.proyecto.backend.model.medication;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class MedicationTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @ManyToOne
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    // -------------------- Fechas --------------------
    private LocalDateTime scheduledTime;
    private LocalDateTime takenTime;

    private Boolean taken;// tomada o pendiente

    // -------------------- Constructores --------------------
    public MedicationTracking() {
    }

    public MedicationTracking(Long id, Medication medication, Prescription prescription, LocalDateTime scheduledTime, LocalDateTime takenTime, Boolean taken) {
        this.id = id;
        this.medication = medication;
        this.prescription = prescription;
        this.scheduledTime = scheduledTime;
        this.takenTime = takenTime;
        this.taken = taken;
    }

    // -------------------- Getters y Setters --------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public LocalDateTime getTakenTime() {
        return takenTime;
    }

    public void setTakenTime(LocalDateTime takenTime) {
        this.takenTime = takenTime;
    }

    public Boolean getTaken() {
        return taken;
    }

    public void setTaken(Boolean taken) {
        this.taken = taken;
    }
}
