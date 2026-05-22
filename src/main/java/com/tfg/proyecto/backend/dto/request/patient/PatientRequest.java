package com.tfg.proyecto.backend.dto.request.patient;

import com.tfg.proyecto.backend.model.users.Patient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
}
