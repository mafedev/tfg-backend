package com.tfg.proyecto.backend.dto.request;

public class MedicationScheduleRequest {
    private Long patientId;
    private Long medicationId;
    private String hour;
    private Integer frecuency;
    private Integer dose;

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

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Integer getFrecuency() {
        return frecuency;
    }

    public void setFrecuency(Integer frecuency) {
        this.frecuency = frecuency;
    }

    public Integer getDose() {
        return dose;
    }

    public void setDose(Integer dose) {
        this.dose = dose;
    }
}
