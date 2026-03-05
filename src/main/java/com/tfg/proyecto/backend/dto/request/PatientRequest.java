package com.tfg.proyecto.backend.dto.request;

import com.tfg.proyecto.backend.model.users.Patient;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class PatientRequest {
    private Long userId;

    @NotNull
    private Patient.DiseaseStage diseaseStage;

    private String clinicalNotes;

    private LocalDate diagnosisDate;

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

    public Patient.DiseaseStage getDiseaseStage() {
        return diseaseStage;
    }

    public void setDiseaseStage(Patient.DiseaseStage diseaseStage) {
        this.diseaseStage = diseaseStage;
    }

    public String getClinicalNotes() {
        return clinicalNotes;
    }

    public void setClinicalNotes(String clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public LocalDate getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(LocalDate diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }
}
