package com.tfg.proyecto.backend.dto.response.routine;

import java.util.List;

public class RoutineResponse {
    private Long id;
    private Long patientId;
    private List<RoutineItemResponse> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public List<RoutineItemResponse> getItems() {
        return items;
    }

    public void setItems(List<RoutineItemResponse> items) {
        this.items = items;
    }
}
