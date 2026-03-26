package com.tfg.proyecto.backend.dto.request;

import com.tfg.proyecto.backend.model.users.Patient;

public class PatientUpdateRequest {

    private String name;
    private String phone;
    private String notes;
    private Patient.DiseaseStage diseaseStage;

    // getters y setters
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Patient.DiseaseStage getDiseaseStage() {
        return diseaseStage;
    }

    public void setDiseaseStage(Patient.DiseaseStage diseaseStage) {
        this.diseaseStage = diseaseStage;
    }
}
