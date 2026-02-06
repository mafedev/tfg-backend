package com.tfg.proyecto.backend.entity_model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "medications_schedules")
public class MedicationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    // Cantidad por dosis
    @Column(nullable = false)
    private int dose;

    // Fecha de inicio del tratamiento
    private LocalDate startDate;

    // Fecha de fin del tratamiento
    private LocalDate endDate;

    // Frecuencia en horas
    private Integer frequencyHours;

    // Hora de la primera dosis
    private LocalTime firstDoseTime;

    // Días específicos de la semana
    @ElementCollection
    @CollectionTable(name = "medication_schedule_days", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "day_of_week")
    private List<Integer> daysOfWeek;

    // -------- Constructores --------
    public MedicationSchedule() {
    }

    public MedicationSchedule(User patient, Medication medication, int dose, LocalDate startDate, LocalDate endDate, Integer frequencyHours, LocalTime firstDoseTime, List<Integer> daysOfWeek) {
        this.patient = patient;
        this.medication = medication;
        this.dose = dose;
        this.startDate = startDate;
        this.endDate = endDate;
        this.frequencyHours = frequencyHours;
        this.firstDoseTime = firstDoseTime;
        this.daysOfWeek = daysOfWeek;
    }

    // -------- Getters y Setters --------
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

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
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

    public Integer getFrequencyHours() {
        return frequencyHours;
    }

    public void setFrequencyHours(Integer frequencyHours) {
        this.frequencyHours = frequencyHours;
    }

    public LocalTime getFirstDoseTime() {
        return firstDoseTime;
    }

    public void setFirstDoseTime(LocalTime firstDoseTime) {
        this.firstDoseTime = firstDoseTime;
    }

    public List<Integer> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<Integer> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
}
