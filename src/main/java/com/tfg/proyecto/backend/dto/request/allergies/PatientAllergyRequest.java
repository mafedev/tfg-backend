package com.tfg.proyecto.backend.dto.request.allergies;

import com.tfg.proyecto.backend.model.allergies.PatientAllergy;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PatientAllergyRequest {
    private String name;
    private String description;
    private PatientAllergy.AllergySeverity severity;

    // -------------------- Constructor --------------------
    public PatientAllergyRequest() {
    }
}
