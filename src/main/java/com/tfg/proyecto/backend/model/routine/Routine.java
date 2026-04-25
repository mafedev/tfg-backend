package com.tfg.proyecto.backend.model.routine;

import com.tfg.proyecto.backend.model.users.Patient;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routines")
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación 1-1 con paciente (un paciente tiene una rutina)
    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    @OneToMany(mappedBy = "routine", fetch = FetchType.EAGER)
    private List<RoutineItem> items = new ArrayList<>();

    // -------------------- Constructores --------------------
    public Routine() {
    }

    public Routine(Long id, Patient patient) {
        this.id = id;
        this.patient = patient;
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

    public List<RoutineItem> getItems() {
        return items;
    }

    public void setItems(List<RoutineItem> items) {
        this.items = items;
    }
}