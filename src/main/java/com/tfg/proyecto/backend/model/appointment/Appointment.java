package com.tfg.proyecto.backend.model.appointment;

import com.tfg.proyecto.backend.model.users.Patient;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id autogenerado
    private Long id;

    @ManyToOne // muchas citas puedes pertenecer a un paciente
    @JoinColumn(name = "patient_id") // define la FK
    private Patient patient;

    private String title;
    private String description;
    private String location;
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING) // guarda el enum como string en la bdd
    // Representa el estado de la cita, por defecto está pendiente
    private AppointmentStatus status = AppointmentStatus.PENDING;

    public enum AppointmentStatus {
        PENDING, ATTENDED, MISSED, CANCELLED
    }

    // -------------------- Constructores --------------------
    public Appointment() {
    }

    public Appointment(Long id, Patient patient, String title, String description, String location, LocalDateTime dateTime, AppointmentStatus status) {
        this.id = id;
        this.patient = patient;
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.status = status;
    }

    // -------------------- Getters y Setters --------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}