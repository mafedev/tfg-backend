package com.tfg.proyecto.backend.dto.request.medication;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ToggleTrackingRequest {
    private Long prescriptionId;
    private LocalDateTime scheduledTime;

    // -------------------- Constructor --------------------
    public ToggleTrackingRequest() {
    }
}