package com.tfg.proyecto.backend.dto.request.allergies;

import com.tfg.proyecto.backend.model.allergies.PatientAllergy;

public class PatientAllergyRequest {
    private String name;
    private String description;
    private PatientAllergy.AllergySeverity severity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PatientAllergy.AllergySeverity getSeverity() {
        return severity;
    }

    public void setSeverity(PatientAllergy.AllergySeverity severity) {
        this.severity = severity;
    }
}
