package com.tfg.proyecto.backend.dto.request.medication;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionTrackingResponse {
    private Long prescriptionId;
    private String medicationName;
    private String dose;
    private List<PrescriptionTrackingTimeResponse> times = new ArrayList<>();

    public PrescriptionTrackingResponse() {
    }

    public PrescriptionTrackingResponse(Long prescriptionId, String medicationName, String dose, List<PrescriptionTrackingTimeResponse> times) {
        this.prescriptionId = prescriptionId;
        this.medicationName = medicationName;
        this.dose = dose;
        this.times = times;
    }

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public List<PrescriptionTrackingTimeResponse> getTimes() {
        return times;
    }

    public void setTimes(List<PrescriptionTrackingTimeResponse> times) {
        this.times = times;
    }

    // ------------ Tiempo de cada toma ------------
    public static class PrescriptionTrackingTimeResponse {
        private String scheduledTime;
        private Boolean taken;
        private String status;

        public PrescriptionTrackingTimeResponse() {
        }

        public PrescriptionTrackingTimeResponse(String scheduledTime, Boolean taken, String status) {
            this.scheduledTime = scheduledTime;
            this.taken = taken;
            this.status = status;
        }

        public String getScheduledTime() {
            return scheduledTime;
        }

        public void setScheduledTime(String scheduledTime) {
            this.scheduledTime = scheduledTime;
        }

        public Boolean getTaken() {
            return taken;
        }

        public void setTaken(Boolean taken) {
            this.taken = taken;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}