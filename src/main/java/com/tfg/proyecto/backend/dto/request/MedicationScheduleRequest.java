package com.tfg.proyecto.backend.dto.request;

import java.util.List;

public class MedicationScheduleRequest {
    private Long patientId;
    private Long medicationId;
    private Integer dose;
    private String startDate;
    private String endDate;
    private Integer frequencyHours;
    private List<Integer> daysOfWeek;
    private String firstDoseTime;

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

    public Integer getDose() {
        return dose;
    }

    public void setDose(Integer dose) {
        this.dose = dose;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getFrequencyHours() {
        return frequencyHours;
    }

    public void setFrequencyHours(Integer frequencyHours) {
        this.frequencyHours = frequencyHours;
    }

    public List<Integer> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<Integer> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getFirstDoseTime() {
        return firstDoseTime;
    }

    public void setFirstDoseTime(String firstDoseTime) {
        this.firstDoseTime = firstDoseTime;
    }
}
