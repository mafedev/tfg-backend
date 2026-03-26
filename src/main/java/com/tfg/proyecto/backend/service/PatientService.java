package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.dto.request.PatientRequest;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient createSimplePatient(PatientRequest request) {
        Patient patient = new Patient();
        patient.setUser(null);
        patient.setDiseaseStage(request.getDiseaseStage());
        patient.setNotes(request.getNotes());
        patientRepository.save(patient);
        return patient;
    }
}