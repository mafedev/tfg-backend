package com.tfg.proyecto.backend.model.medication;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "medications")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicationType type;

    public enum MedicationType {
        TABLET,
        COATED_TABLET,
        EFFERVESCENT_TABLET,
        SUBLINGUAL_TABLET,
        CHEWABLE_TABLET,
        HARD_CAPSULE,
        SOFT_CAPSULE,
        DRAGEE,
        TABLET_FORM,
        PILL,
        POWDER,
        GRANULES,
        SACHET,
        SYRUP,
        ORAL_SOLUTION,
        ORAL_SUSPENSION,
        ORAL_EMULSION,
        ORAL_DROPS,
        OPHTHALMIC_DROPS,
        OTIC_DROPS,
        NASAL_DROPS,
        INTRAMUSCULAR_INJECTION,
        INTRAVENOUS_INJECTION,
        SUBCUTANEOUS_INJECTION,
        INTRAVENOUS_SOLUTION,
        AMPOULE,
        VIAL,
        CREAM,
        OINTMENT,
        GEL,
        LOTION,
        FOAM,
        SPRAY,
        AEROSOL,
        INHALER,
        NEBULIZER,
        TRANSDERMAL_PATCH,
        SUPPOSITORY,
        VAGINAL_OVULE,
        ENEMA,
        EYE_DROPS,
        MOUTHWASH,
        IMPLANT,
        LYOPHILIZED
    }

    // -------------------- Constructores --------------------
    public Medication() {
    }

    public Medication(String name, String description, MedicationType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    // -------------------- Getters y Setters --------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MedicationType getType() {
        return type;
    }

    public void setType(MedicationType type) {
        this.type = type;
    }
}
