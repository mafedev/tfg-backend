package com.tfg.proyecto.backend.model;

import com.tfg.proyecto.backend.model.users.Caregiver;
import com.tfg.proyecto.backend.model.users.Patient;
import jakarta.persistence.*;

@Entity
// Se usa el UniqueConstraint para evitar tener múltiples relaciones entre los mismos usuarios
@Table(name = "relations", uniqueConstraints = {@UniqueConstraint(columnNames = {"caregiver_id", "patient_id"})})
public class Relation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "caregiver_id", nullable = false)
    private Caregiver caregiver;

    @ManyToOne(optional = true)
    @JoinColumn(name = "patient_id", nullable = true)
    private Patient patient;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    // -------------------- Contructores --------------------
    public Relation() {
    }

    public Relation(Long id, Caregiver caregiver, Patient patient, String code) {
        this.id = id;
        this.caregiver = caregiver;
        this.patient = patient;
        this.code = code;
    }

    // -------------------- Getters y Setters --------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Caregiver getCaregiver() {
        return caregiver;
    }

    public void setCaregiver(Caregiver caregiver) {
        this.caregiver = caregiver;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
