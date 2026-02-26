package com.tfg.proyecto.backend.dto.request;

import com.tfg.proyecto.backend.model.medication.Prescription;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PrescriptionRequest {

    @NotNull
    private Long patientId;

    @NotNull
    private Long medicationId;

    @Positive
    private BigDecimal dose;

    @NotNull
    private Prescription.DoseUnit doseUnit;

    private Integer frequencyHours;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime firstDoseTime;
    private List<DayOfWeek> daysOfWeek;
    private String notes;

    // -------------------- Constructor --------------------
    public PrescriptionRequest() {
    }

    // -------------------- Getters y Setters --------------------
    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(Long medicationId) {
        this.medicationId = medicationId;
    }

    public BigDecimal getDose() {
        return dose;
    }

    public void setDose(BigDecimal dose) {
        this.dose = dose;
    }

    public Prescription.DoseUnit getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(Prescription.DoseUnit doseUnit) {
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

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
