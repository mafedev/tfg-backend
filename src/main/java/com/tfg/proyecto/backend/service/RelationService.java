package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.dto.response.PatientListResponse;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.Relation;
import com.tfg.proyecto.backend.model.users.User;
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
    private final PatientRepository patientRepository;

    public RelationService(RelationRepository relationRepository, UserRepository userRepository, PatientRepository patientRepository) {
        this.relationRepository = relationRepository;
        this.userRepository = userRepository;
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
        Optional<User> caregiverRep = userRepository.findById(caregiverId);
        if (caregiverRep.isPresent() && caregiverRep.get().getRole() == User.Role.CAREGIVER) {
            User caregiver = caregiverRep.get();

            Relation relation = new Relation();
            relation.setCaregiver(caregiver);
            relation.setPatient(null);
            relation.setCode(generateCode());

            return relationRepository.save(relation);
        }
        return null;
    }

    // se llama cuando el paciente ingresa el código
    public boolean linkPatient(String code, Long userId) {

        // 1. Validar que el usuario existe
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            System.out.println("[ERROR] Usuario no encontrado: " + userId);
            return false;
        }

        User user = userOpt.get();

        // 2. Validar que es paciente
        if (user.getRole() != User.Role.PATIENT) {
            System.out.println("[ERROR] El usuario no es PATIENT: " + userId);
            return false;
        }

        // 3. Buscar el Patient asociado a ese User
        Optional<Patient> patientOpt = patientRepository.findByUserId(userId);
        if (patientOpt.isEmpty()) {
            System.out.println("[ERROR] No existe Patient para userId=" + userId);
            return false;
        }

        Patient patient = patientOpt.get();
        System.out.println("[DEBUG] Paciente encontrado: " + patient.getId());

        // 4. Buscar relación por código
        Optional<Relation> relationOpt = relationRepository.findByCode(code);
        if (relationOpt.isEmpty()) {
            System.out.println("[ERROR] Código inválido: " + code);
            return false;
        }

        Relation relation = relationOpt.get();

        // 5. Verificar si ya está vinculado
        if (relation.getPatient() != null) {
            System.out.println("[ERROR] Código ya usado por patientId=" + relation.getPatient().getId());
            return false;
        }

        // 6. Vincular
        relation.setPatient(patient);
        relationRepository.save(relation);

        System.out.println("[DEBUG] Paciente vinculado correctamente");

        return true;
    }

    public List<PatientListResponse> getPatientsList(Long caregiverId) {
        List<PatientListResponse> patients = new ArrayList<>();

        // busca el usuario por el id
        Optional<User> caregiverResp = userRepository.findById(caregiverId);

        // si el paciente existe, entonces lo agrega a la lista
        if (caregiverResp.isPresent() && caregiverResp.get().getRole() == User.Role.CAREGIVER) {
            List<Relation> matchedRelations = relationRepository.findByCaregiverIdAndPatientIdIsNotNull(caregiverId); // lista de ids de los pacientes

            for (Relation relation : matchedRelations) {
                Patient patient = relation.getPatient();
                if (patient != null) {

                    String name;
                    String phone;

                    // Si tiene usuario asociado
                    if (patient.getUser() != null) {
                        name = patient.getUser().getUsername();
                        phone = patient.getUser().getPhone();
                    } else {
                        // Si NO tiene usuario (paciente sin cuenta)
                        name = patient.getName();
                        phone = patient.getPhone();
                    }

                    patients.add(new PatientListResponse(
                            patient.getId(),
                            name,
                            phone
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
