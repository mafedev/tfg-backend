package com.tfg.proyecto.backend.controller.allergies;

import com.tfg.proyecto.backend.dto.request.allergies.PatientAllergyRequest;
import com.tfg.proyecto.backend.model.allergies.PatientAllergy;
import com.tfg.proyecto.backend.service.allergies.PatientAllergyService;
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

    // -------------------- DELETE --------------------
    @DeleteMapping("/{allergyId}")
    public void delete(@PathVariable Long patientId, @PathVariable Long allergyId) {
        service.delete(patientId, allergyId);
    }

    // -------------------- UPDATE --------------------
    @PutMapping("/{allergyId}")
    public PatientAllergy updateSeverity(@PathVariable Long patientId, @PathVariable Long allergyId, @RequestBody PatientAllergyRequest request) {
        return service.updateSeverity(patientId, allergyId, request.getSeverity());
    }
}