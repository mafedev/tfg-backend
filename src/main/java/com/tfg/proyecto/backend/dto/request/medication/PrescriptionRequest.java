package com.tfg.proyecto.backend.dto.request.medication;

import com.tfg.proyecto.backend.model.medication.Prescription;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class PrescriptionRequest {
    @NotNull(message = "El paciente es obligatorio")
    private Long patientId;

    @NotNull(message = "El medicamento es obligatorio")
    private Long medicationId;

    @NotNull(message = "La dosis es obligatoria")
    @Positive(message = "La dosis debe ser mayor que 0")
    private BigDecimal dose;

    @NotNull(message = "La unidad de dosis es obligatoria")
    private Prescription.DoseUnit doseUnit;

    @Positive(message = "La frecuencia debe ser mayor que 0")
    private Integer frequencyHours;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime firstDoseTime;

    @Size(max = 500, message = "Las notas no pueden superar los 500 caracteres")
    private String notes;

    private Prescription.PrescriptionStatus status;

    // -------------------- Constructor --------------------
    public PrescriptionRequest() {
    }
}