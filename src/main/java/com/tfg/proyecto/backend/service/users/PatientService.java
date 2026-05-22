package com.tfg.proyecto.backend.service.users;

import com.tfg.proyecto.backend.dto.request.patient.PatientRequest;
import com.tfg.proyecto.backend.model.relation.Relation;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.relation.RelationRepository;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import com.tfg.proyecto.backend.repository.users.UserRepository;
import com.tfg.proyecto.backend.security.SecurityUtils;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final RelationRepository relationRepository;
    private final SecurityUtils securityUtils;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository, RelationRepository relationRepository, SecurityUtils securityUtils) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.relationRepository = relationRepository;
        this.securityUtils = securityUtils;
    }

    public Patient createPatient(PatientRequest request) {
        Long caregiverId = securityUtils.getCurrentUserId();
        User caregiver = userRepository.findById(caregiverId).orElseThrow(() -> new RuntimeException("Cuidador no encontrado"));

        Patient patient = new Patient();

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            patient.setUser(user);
        }

        patient.setDiseaseStage(request.getDiseaseStage());
        patient.setNotes(request.getNotes());
        patient.setName(request.getName());
        patient.setPhone(request.getPhone());
        Patient savedPatient = patientRepository.save(patient);
        relationRepository.save(new Relation(null, caregiver, savedPatient, null));

        return savedPatient;
    }
}