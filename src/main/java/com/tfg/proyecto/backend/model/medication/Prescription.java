package com.tfg.proyecto.backend.model.medication;

import com.tfg.proyecto.backend.model.users.Patient;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) // un paciente puede tener muchas preescripciones
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @Column(nullable = false)
    private BigDecimal dose; // BigDecimal para más precisión

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoseUnit doseUnit;

    private Integer frequencyHours;

    // Fecha de inicio del tratamiento
    private LocalDate startDate;

    // Fecha de fin del tratamiento
    private LocalDate endDate;

    // Hora de la primera dosis
    private LocalTime firstDoseTime;

    @Column(length = 500)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrescriptionStatus status = PrescriptionStatus.ACTIVE; // valor por defecto

    public enum PrescriptionStatus {
        ACTIVE,
        SUSPENDED,
        COMPLETED,
        CANCELLED
    }

    public enum DoseUnit {
        MG,
        ML,
        CAPSULE,
        TABLET,
        DROP,
        INHALATION,
        PATCH
    }

    // -------------------- Constructores --------------------
    public Prescription() {
    }

    public Prescription(Long id, Patient patient, Medication medication, BigDecimal dose, DoseUnit doseUnit, Integer frequencyHours, LocalDate startDate, LocalDate endDate, LocalTime firstDoseTime, String notes, PrescriptionStatus status) {
        this.id = id;
        this.patient = patient;
        this.medication = medication;
        this.dose = dose;
        this.doseUnit = doseUnit;
        this.frequencyHours = frequencyHours;
        this.startDate = startDate;
        this.endDate = endDate;
        this.firstDoseTime = firstDoseTime;
        this.notes = notes;
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

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public BigDecimal getDose() {
        return dose;
    }

    public void setDose(BigDecimal dose) {
        this.dose = dose;
    }

    public DoseUnit getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(DoseUnit doseUnit) {
        this.doseUnit = doseUnit;
    }

    public Integer getFrequencyHours() {
        return frequencyHours;
    }

    public void setFrequencyHours(Integer frequencyHours) {
        this.frequencyHours = frequencyHours;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getFirstDoseTime() {
        return firstDoseTime;
    }

    public void setFirstDoseTime(LocalTime firstDoseTime) {
        this.firstDoseTime = firstDoseTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public PrescriptionStatus getStatus() {
        return status;
    }

    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }
}
