package com.tfg.proyecto.backend.dto.request;

import com.tfg.proyecto.backend.model.medication.Medication;

public class MedicationRequest {

    private String name;
    private String description;
    private Medication.MedicationType type;

    public MedicationRequest() {
    }

    public MedicationRequest(String name, String description, Medication.MedicationType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

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

    public Medication.MedicationType getType() {
        return type;
    }

    public void setType(Medication.MedicationType type) {
        this.type = type;
    }
}
