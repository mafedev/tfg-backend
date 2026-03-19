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
    public Relation createRelation(Long caregiverId) {
        Optional<Caregiver> caregiverOpt = caregiverRepository.findByUserId(caregiverId);

        if (caregiverOpt.isEmpty()) {
            return null;
        }

        Caregiver caregiver = caregiverOpt.get();

        Relation relation = new Relation();
        relation.setCaregiver(caregiver);
        relation.setPatient(null);
        relation.setCode(generateCode());

        return relationRepository.save(relation);
    }

    // se llama cuando el paciente ingresa el código
    public boolean linkPatient(String code, Long patientUserId) {

        // Obtiene el user si existe
        Optional<User> userRepositoryResponse = userRepository.findById(patientUserId);
        if (userRepositoryResponse.isEmpty()) { // si no existe, devuelve false
            return false;
        }

        User user = userRepositoryResponse.get(); // obtiene el user

        // se asegura que el user sea PACIENTE
        if (user.getRole() == User.Role.PATIENT) {

            // obtiene la relación con ese código si existe
            Optional<Relation> relationRepositoryResponse = relationRepository.findByCode(code);

            if (relationRepositoryResponse.isEmpty()) { // si no existe la relación
                return false;
            }

            Relation relation = relationRepositoryResponse.get(); // obtiene la relación

            // si ya hay un paciente asignado
            if (relation.getPatient() != null) {
                return false;
            }

            // obtiene el paciente asociado al user
            Optional<Patient> patientRepositoryResponse = patientRepository.findByUserId(user.getId());

            if (patientRepositoryResponse.isEmpty()) { // si no existe el paciente
                return false;
            }

            // se asegura que haya un cuidador y no haya paciente aún
            if (relation.getCaregiver() != null && relation.getPatient() == null) {

                Patient patient = patientRepositoryResponse.get();

                relation.setPatient(patient);
                relationRepository.save(relation);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public List<PatientListResponse> getPatientsList(Long caregiverId) {
        List<PatientListResponse> patients = new ArrayList<>();

        Optional<User> caregiverResp = userRepository.findById(caregiverId);
        if (caregiverResp.isPresent() && caregiverResp.get().getRole() == User.Role.CAREGIVER) {
            List<Relation> matchedRelations = relationRepository.findByCaregiverIdAndPatientIdIsNotNull(caregiverId);

            for (Relation relation : matchedRelations) {
                Patient patient = relation.getPatient();
                if (patient != null) {
                    patients.add(new PatientListResponse(
                            patient.getId(),
                            patient.getName(),
                            patient.getPhone()
                    ));
                }
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
