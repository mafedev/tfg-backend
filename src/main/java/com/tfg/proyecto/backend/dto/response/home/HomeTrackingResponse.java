package com.tfg.proyecto.backend.dto.response.home;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HomeTrackingResponse {

    private Long trackingId;
    private Long prescriptionId;

    private String medicationName;
    private String patientName;

    private String scheduledTime;
    private String status;

    // -------------------- Constructor --------------------
    public HomeTrackingResponse() {
    }

    public HomeTrackingResponse(Long trackingId, Long prescriptionId, String medicationName, String patientName, String scheduledTime, String status) {
        this.trackingId = trackingId;
        this.prescriptionId = prescriptionId;
        this.medicationName = medicationName;
        this.patientName = patientName;
        this.scheduledTime = scheduledTime;
        this.status = status;
    }
}