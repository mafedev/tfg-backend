package com.tfg.proyecto.backend.dto.response;

public class PatientListResponse {
    private Long patientId;
    private String name;
    private String phone;

    // -------------------- Constructor --------------------
    public PatientListResponse(Long patientId, String name, String phone) {
        this.patientId = patientId;
        this.name = name;
        this.phone = phone;
    }

    // -------------------- Getters --------------------
    public Long getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
