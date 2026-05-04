package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.model.allergies.Allergy;
import com.tfg.proyecto.backend.model.allergies.PatientAllergy;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.allergies.AllergyRepository;
import com.tfg.proyecto.backend.repository.allergies.PatientAllergyRepository;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientAllergyService {

    private final PatientAllergyRepository patientAllergyRepository;
    private final PatientRepository patientRepository;
    private final AllergyRepository allergyRepository;

    public PatientAllergyService(PatientAllergyRepository patientAllergyRepository, PatientRepository patientRepository, AllergyRepository allergyRepository) {
        this.patientAllergyRepository = patientAllergyRepository;
        this.patientRepository = patientRepository;
        this.allergyRepository = allergyRepository;
    }

    // -------------------- GET --------------------
    public List<PatientAllergy> getByPatient(Long patientId) {
        return patientAllergyRepository.findByPatientId(patientId);
    }

    // -------------------- CREATE --------------------
    public PatientAllergy addAllergy(Long patientId, String allergyName, String description, PatientAllergy.AllergySeverity severity) {

        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isEmpty()) return null;

        Allergy allergy = allergyRepository.findByNameIgnoreCase(allergyName).orElseGet(() -> {
            Allergy newAllergy = new Allergy();
            newAllergy.setName(allergyName);
            newAllergy.setDescription(description);
            return allergyRepository.save(newAllergy);
        });

        PatientAllergy pa = new PatientAllergy();
        pa.setPatient(patientOpt.get());
        pa.setAllergy(allergy);
        pa.setSeverity(severity);

        return patientAllergyRepository.save(pa);
    }
}