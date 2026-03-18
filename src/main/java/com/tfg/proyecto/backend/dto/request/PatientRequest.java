package com.tfg.proyecto.backend.dto.request;

import com.tfg.proyecto.backend.model.users.Patient;
import jakarta.validation.constraints.NotNull;

public class PatientRequest {
    private Long userId;
    private String name;
    private String phone;
    private String notes;

    @NotNull
    private Patient.DiseaseStage diseaseStage;

    // -------------------- Constructor --------------------
    public PatientRequest() {
    }

    // -------------------- Getters y Setters --------------------
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Patient.DiseaseStage getDiseaseStage() {
        return diseaseStage;
    }

    public void setDiseaseStage(Patient.DiseaseStage diseaseStage) {
        this.diseaseStage = diseaseStage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
