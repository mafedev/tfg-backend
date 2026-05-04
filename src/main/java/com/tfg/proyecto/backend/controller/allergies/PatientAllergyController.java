package com.tfg.proyecto.backend.controller.allergies;

import com.tfg.proyecto.backend.dto.request.allergies.PatientAllergyRequest;
import com.tfg.proyecto.backend.model.allergies.PatientAllergy;
import com.tfg.proyecto.backend.service.PatientAllergyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/allergies")
public class PatientAllergyController {

    private final PatientAllergyService service;

    public PatientAllergyController(PatientAllergyService service) {
        this.service = service;
    }

    // -------------------- GET --------------------
    @GetMapping
    public List<PatientAllergy> getAll(@PathVariable Long patientId) {
        return service.getByPatient(patientId);
    }

    // -------------------- CREATE --------------------
    @PostMapping
    public PatientAllergy create(@PathVariable Long patientId, @RequestBody PatientAllergyRequest request) {
        return service.addAllergy(patientId, request.getName(), request.getDescription(), request.getSeverity());
    }
}