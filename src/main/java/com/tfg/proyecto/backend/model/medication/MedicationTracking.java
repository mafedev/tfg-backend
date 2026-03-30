package com.tfg.proyecto.backend.model.medication;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class MedicationTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    // -------------------- Fechas --------------------
    private LocalDateTime scheduledTime;
    private LocalDateTime takenTime;

    private Boolean taken;// tomada o pendiente

    // Estado de la toma
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrackingStatus status;

    public enum TrackingStatus {
        PENDING, TAKEN, SKIPPED
    }

    // -------------------- Constructores --------------------
    public MedicationTracking() {
    }

    public MedicationTracking(Long id, Prescription prescription, LocalDateTime scheduledTime, LocalDateTime takenTime, Boolean taken, TrackingStatus status) {
        this.id = id;
        this.prescription = prescription;
        this.scheduledTime = scheduledTime;
        this.takenTime = takenTime;
        this.taken = taken;
        this.status = status;
    }

    // -------------------- Getters y Setters --------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public TrackingStatus getStatus() {
        return status;
    }

    public void setStatus(TrackingStatus status) {
        this.status = status;
    }
}