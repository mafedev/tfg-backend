package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.dto.response.PatientListResponse;
import com.tfg.proyecto.backend.entity_model.Relation;
import com.tfg.proyecto.backend.entity_model.User;
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

    public RelationService(RelationRepository relationRepository, UserRepository userRepository) {
        this.relationRepository = relationRepository;
        this.userRepository = userRepository;
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
        Optional<User> caregiverRep = userRepository.findById(caregiverId);
        if (caregiverRep.isPresent()) {
            User caregiver = caregiverRep.get();

            if (caregiver.getRole().equals("CUIDADOR")) {

                Relation relation = new Relation();
                relation.setCaregiverId(caregiverId);
                relation.setCode(generateCode());

                return relationRepository.save(relation);
            }
            return null;
        }
        return null;
    }

    // se llama cuando el paciente ingresa el código
    public boolean linkPatient(String code, Long patientId) {

        // Obtiene el user si existe
        Optional<User> userRepositoryResponse = userRepository.findById(patientId);
        if (userRepositoryResponse.isEmpty()) { // si no existe, devuelve null
            return false;
        }

        User user = userRepositoryResponse.get(); // obtiene el user

        if (user.getRole().equals("PACIENTE")) { // se asegura que el user sea PACIENTE, para evitar relaciones entre CUIDADORES
            Optional<Relation> repositoryResponse = relationRepository.findByCode(code); // obtiene la relación con ese código si existe

            if (repositoryResponse.isEmpty()) { // si no existe la relación
                return false;
            }
            // se asegura que no sea null
            Relation relation = repositoryResponse.get();

            if (relation.getPatientId() != null) { // si ya hay alguien registrado
                return false;
            }

            // si la relación entre paciente-cuidador no existe, entonces la guarda
            if (!relationRepository.existsByCaregiverIdAndPatientId(relation.getCaregiverId(), patientId)) {
                relation.setPatientId(patientId);
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

        // busca el usuario por el id
        Optional<User> caregiverResp = userRepository.findById(caregiverId);

        // si el paciente existe, entonces lo agrega a la lista
        if (caregiverResp.isPresent() && caregiverResp.get().getRole().equals("CUIDADOR")) {
            User caregiver = caregiverResp.get();

            List<Relation> matchedRelations = relationRepository.findByCaregiverIdAndPatientIdIsNotNull(caregiverId); // lista de ids de los pacientes

            for (Relation relation : matchedRelations) {
                Optional<User> patientOpt = userRepository.findById(relation.getPatientId());
                if (patientOpt.isPresent()) {
                    User patient = patientOpt.get();
                    patients.add(new PatientListResponse(patient.getId(), patient.getUsername()));
                }
            }
        }
        return patients;
    }
}
