package com.tfg.proyecto.backend.model.allergies;

import com.tfg.proyecto.backend.model.users.Patient;
import jakarta.persistence.*;

@Entity
// para evitar que el paciente tenga registrada la misma alergía registrada varias veces
@Table(name = "patients_allergies", uniqueConstraints = {@UniqueConstraint(columnNames = {"patient_id", "allergy_id"})})
public class PatientAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "allergy_id", nullable = false)
    private Allergy allergy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AllergySeverity severity;

    public enum AllergySeverity {
        LOW, MODERATE, HIGH, CRITICAL
    }

    // -------------------- Constructores --------------------
    public PatientAllergy() {
    }

    public PatientAllergy(Long id, Patient patient, Allergy allergy, AllergySeverity severity) {
        this.id = id;
        this.patient = patient;
        this.allergy = allergy;
        this.severity = severity;
    }

    // Getters y Setters
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

    public Allergy getAllergy() {
        return allergy;
    }

    public void setAllergy(Allergy allergy) {
        this.allergy = allergy;
    }

    public AllergySeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AllergySeverity severity) {
        this.severity = severity;
    }
}
