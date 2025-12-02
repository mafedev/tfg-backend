package com.tfg.proyecto.backend.entity_model;

import jakarta.persistence.*;

@Entity
@Table(name = "relations")
public class Relation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_caregiver", nullable = false)
    private Long caregiverId;

    @Column(name = "id_patient", nullable = false)
    private Long patientId;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    // Contructores

    public Relation() {
    }

    public Relation(Long id, Long caregiverId, Long patientId, String code) {
        this.id = id;
        this.caregiverId = caregiverId;
        this.patientId = patientId;
        this.code = code;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(Long caregiverId) {
        this.caregiverId = caregiverId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
