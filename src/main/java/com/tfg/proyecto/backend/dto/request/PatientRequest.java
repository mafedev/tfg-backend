package com.tfg.proyecto.backend.dto.request;

import com.tfg.proyecto.backend.model.Patient;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class PatientRequest {
    private Long userId;

    @NotNull
    private Patient.DiseaseStage diseaseStage;

    private String clinicalNotes;

    private LocalDate diagnosisDate;
}
