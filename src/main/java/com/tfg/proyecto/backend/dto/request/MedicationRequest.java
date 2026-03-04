package com.tfg.proyecto.backend.dto.request;

import com.tfg.proyecto.backend.model.medication.Medication;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MedicationRequest {

    @NotBlank
    private String name;
    private String description;

    @NotNull
    private Medication.MedicationType type;

    // -------------------- Constructor --------------------
    public MedicationRequest() {
    }

    // -------------------- Getters y Setters --------------------
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
