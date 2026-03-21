package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.dto.response.PatientListResponse;
import com.tfg.proyecto.backend.model.users.Caregiver;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.Relation;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.CaregiverRepository;
import com.tfg.proyecto.backend.repository.PatientRepository;
import com.tfg.proyecto.backend.repository.RelationRepository;
import com.tfg.proyecto.backend.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RelationService {

    // inyección de dependencias
    private final RelationRepository relationRepository;
    private final UserRepository userRepository;
    private final CaregiverRepository caregiverRepository;
    private final PatientRepository patientRepository;

    public RelationService(RelationRepository relationRepository, UserRepository userRepository, CaregiverRepository caregiverRepository, PatientRepository patientRepository) {
        this.relationRepository = relationRepository;
        this.userRepository = userRepository;
        this.caregiverRepository = caregiverRepository;
        this.patientRepository = patientRepository;
    }

    // Genera el código que introduce el paciente
    private String generateCode() {
        String code;
        do {
            code = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        } while (relationRepository.existsByCode(code));
        return code;
    }

    // Crea la "relación", solo con el id del cuidador y el código
    // para que luego cuando el paciente lo ingrese, se agrega a la fila/relación
    public Relation createRelation(Long caregiverUserId) {
        Optional<Caregiver> caregiverOpt = caregiverRepository.findByUserId(caregiverUserId);

        Caregiver caregiver;
        if (caregiverOpt.isEmpty()) {
            System.out.println("[DEBUG] Caregiver no encontrado para user_id: " + caregiverUserId + ", creando uno automáticamente...");
            caregiver = new Caregiver();
            caregiver.setUser(userRepository.findById(caregiverUserId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado para crear caregiver")));
            caregiver.setName("Cuidador " + caregiverUserId);
            caregiver.setPhone("000000000");
            caregiver = caregiverRepository.save(caregiver);
        } else {
            caregiver = caregiverOpt.get();
            System.out.println("[DEBUG] Caregiver encontrado: " + caregiver.getName() + ", id=" + caregiver.getId());
        }

        Relation relation = new Relation();
        relation.setCaregiver(caregiver);
        relation.setPatient(null);
        relation.setCode(generateCode());

        System.out.println("[DEBUG] Relation creada con código: " + relation.getCode());

        return relationRepository.save(relation);
    }

    // se llama cuando el paciente ingresa el código
    public boolean linkPatient(String code, Long patientUserId) {
        Optional<Patient> patientOpt = patientRepository.findByUserId(patientUserId);

        Patient patient;
        if (patientOpt.isEmpty()) {
            System.out.println("[DEBUG] linkPatient - No se encontró paciente con userId=" + patientUserId);
            return false; // o aquí podrías crear automáticamente al paciente si quieres
        } else {
            patient = patientOpt.get();
            System.out.println("[DEBUG] linkPatient - Paciente encontrado: " + patient.getName() + ", id=" + patient.getId());
        }

        Optional<Relation> relationOpt = relationRepository.findByCode(code);

        if (relationOpt.isEmpty()) {
            System.out.println("[ERROR] linkPatient - Código de relación inválido: " + code);
            return false;
        }

        Relation relation = relationOpt.get();

        if (relation.getPatient() != null) {
            System.out.println("[ERROR] linkPatient - Código ya vinculado a paciente: " + relation.getPatient().getName());
            return false;
        }

        relation.setPatient(patient);
        relationRepository.save(relation);
        System.out.println("[DEBUG] linkPatient - Paciente " + patient.getName() + " vinculado con éxito al código: " + code);

        return true;
    }

    public List<PatientListResponse> getPatientsList(Long userId) {
        List<PatientListResponse> patients = new ArrayList<>();

        Optional<Caregiver> caregiverOpt = caregiverRepository.findByUserId(userId);

        if (caregiverOpt.isEmpty()) return patients;

        Caregiver caregiver = caregiverOpt.get();

        List<Relation> relations = relationRepository
                .findByCaregiverIdAndPatientIdIsNotNull(caregiver.getId());

        for (Relation relation : relations) {
            Patient patient = relation.getPatient();
            if (patient != null) {
                patients.add(new PatientListResponse(
                        patient.getId(),
                        patient.getName(),
                        patient.getPhone()
                ));
            }
        }

        return patients;
    }

    public boolean deleteRelation(Long caregiverId, Long patientId) {
        Relation relation = relationRepository.findByCaregiverIdAndPatientId(caregiverId, patientId); // busca la relación por el id de ambos
        if (relation != null) { // si existe
            relationRepository.delete(relation); // la elimina
            return true;
        }
        return false;
    }
}
