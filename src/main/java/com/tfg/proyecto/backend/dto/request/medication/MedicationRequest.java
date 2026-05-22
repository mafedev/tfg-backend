package com.tfg.proyecto.backend.dto.request.medication;

import com.tfg.proyecto.backend.model.medication.Medication;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MedicationRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String name;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String description;

    @NotNull(message = "El tipo de medicamento es obligatorio")
    private Medication.MedicationType type;

    // -------------------- Constructor --------------------
    public MedicationRequest() {
    }
}