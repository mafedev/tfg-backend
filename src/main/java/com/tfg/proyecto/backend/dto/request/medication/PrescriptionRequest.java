package com.tfg.proyecto.backend.dto.request.medication;

import com.tfg.proyecto.backend.model.medication.Prescription;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PrescriptionRequest {

    @NotNull(message = "El paciente es obligatorio")
    private Long patientId;

    @NotNull(message = "El medicamento es obligatorio")
    private Long medicationId;

    @NotNull(message = "La dosis es obligatoria")
    @Positive(message = "La dosis debe ser mayor que 0")
    private BigDecimal dose;

    @NotNull(message = "La unidad de dosis es obligatoria")
    private Prescription.DoseUnit doseUnit;

    @Positive(message = "La frecuencia debe ser mayor que 0")
    private Integer frequencyHours;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime firstDoseTime;
    private List<DayOfWeek> daysOfWeek;

    @Size(max = 500, message = "Las notas no pueden superar los 500 caracteres")
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