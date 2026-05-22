package com.tfg.proyecto.backend.dto.response.patient;

import com.tfg.proyecto.backend.model.users.Patient.DiseaseStage;
import lombok.Getter;

@Getter
public class PatientListDetailsResponse {
    private Long id;
    private String username;
    private String phone;
    private DiseaseStage diseaseStage;
    private String notes;

    // -------------------- Constructor --------------------
    public PatientListDetailsResponse(Long id, String username, String phone, DiseaseStage diseaseStage, String notes) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.diseaseStage = diseaseStage;
        this.notes = notes;
    }
}