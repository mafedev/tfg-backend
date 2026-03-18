package com.tfg.proyecto.backend.dto.response;

import com.tfg.proyecto.backend.model.users.Patient.DiseaseStage;

public class PatientListDetailsResponse {
    private Long patientId;
    private String name;
    private String phone;
    private DiseaseStage diseaseStage;
    private String notes;

    // -------------------- Constructor --------------------
    public PatientListDetailsResponse(Long patientId, String name, String phone, DiseaseStage diseaseStage, String notes) {
        this.patientId = patientId;
        this.name = name;
        this.phone = phone;
        this.diseaseStage = diseaseStage;
        this.notes = notes;
    }

    // -------------------- Getters --------------------
    public Long getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public DiseaseStage getDiseaseStage() {
        return diseaseStage;
    }

    public String getNotes() {
        return notes;
    }
}