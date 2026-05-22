package com.tfg.proyecto.backend.dto.response.home;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HomePendingTrackingResponse {
    private Long trackingId;
    private Long prescriptionId;

    private String medicationName;
    private String patientName;

    private String scheduledTime;

    // -------------------- Constructor --------------------
    public HomePendingTrackingResponse() {
    }
}