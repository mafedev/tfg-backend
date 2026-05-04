package com.tfg.proyecto.backend.dto.request.medication;

import java.time.LocalDateTime;

public class ToggleTrackingRequest {
    private Long prescriptionId;
    private LocalDateTime scheduledTime;

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}