package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.dto.response.PatientResponse;
import com.tfg.proyecto.backend.entity_model.Relation;
import com.tfg.proyecto.backend.entity_model.User;
import com.tfg.proyecto.backend.repository.RelationRepository;
import com.tfg.proyecto.backend.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RelationService {

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private UserRepository userRepository;

    // Genera el código que introduce el paciente
    private String generateCode() {
        String code;
        do {
            code = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        } while (relationRepository.existsByCode(code));
        return code;
    }

    // Crea la "relación", solo con el id del cuidador y el código
    // para que luego cuando el paciente lo ingrese se agrega a la fila/relación
    public Relation createRelation(Long caregiverId) {
        Relation relation = new Relation();

        relation.setCaregiverId(caregiverId);
        relation.setCode(generateCode());

        return relationRepository.save(relation);
    }

    // se llama cuando el paciente ingresa el código
    public Relation linkPatient(String code, Long patientId) {

        // Obtiene el user si existe
        Optional<User> userRepositoryResponse = userRepository.findById(patientId);
        if (userRepositoryResponse.isEmpty()) { // si no existe, devuelve null
            return null;
        }

        User user = userRepositoryResponse.get(); // obtiene el user

        if (user.getRole().equals("PACIENTE")) { // se asegura que el user sea PACIENTE, para evitar relaciones entre CUIDADORES
            Optional<Relation> repositoryResponse = this.relationRepository.findByCode(code); // obtiene la relación con ese código si existe

            if (repositoryResponse.isEmpty()) { // si no existe la relación
                return null;
            }
            // se asegura que no sea null
            Relation relation = repositoryResponse.get();

            if (relation.getPatientId() != null) { // si ya hay alguien registrado
                return null;
            }

            // si la relación entre paciente-cuidador no existe, entonces la guarda
            if (!relationRepository.existsByCaregiverIdAndPatientId(relation.getCaregiverId(), patientId)) {
                relation.setPatientId(patientId);
                return relationRepository.save(relation);
            } else {
                return null;
            }
        } else{
            return null;
        }

    }

    public List<PatientResponse> getPatientsList(Long caregiverId){
        List<PatientResponse> patients = new ArrayList<>();
        List<Relation> matchedRelations = relationRepository.findByCaregiverIdAndPatientIdIsNotNull(caregiverId); // lista de ids de los pacientes

        for(Relation id: matchedRelations){
            // busca el usuario por el id
            Optional<User> patientRep = userRepository.findById(id.getPatientId());

            // si el paciente existe, entonces lo agrega a la lista
            if(patientRep.isPresent()){
                User patient = patientRep.get();

                PatientResponse dto = new PatientResponse(patient.getId(), patient.getUsername());

                patients.add(dto);
            }

        }
        return patients;
    }
}
