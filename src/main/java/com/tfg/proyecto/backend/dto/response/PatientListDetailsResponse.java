package com.tfg.proyecto.backend.dto.response;

import com.tfg.proyecto.backend.model.users.Patient.DiseaseStage;

public class PatientListDetailsResponse {
    private Long id;
    private String username;
    private String phone;
    private DiseaseStage diseaseStage;
    private String notes;

    // Constructor actualizado
    public PatientListDetailsResponse(Long id, String username, String phone,
                                      DiseaseStage diseaseStage, String notes) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.diseaseStage = diseaseStage;
        this.notes = notes;
    }

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPhone() { return phone; }
    public DiseaseStage getDiseaseStage() { return diseaseStage; }
    public String getNotes() { return notes; }
}