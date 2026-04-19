package com.tfg.proyecto.backend.dto.request.patient;

import com.tfg.proyecto.backend.model.users.Patient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PatientRequest {
    private Long userId; // opcional

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String name;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    private String phone;

    @Size(max = 1000, message = "Las notas no pueden superar los 1000 caracteres")
    private String notes;

    @NotNull(message = "La etapa de la enfermedad es obligatoria")
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
