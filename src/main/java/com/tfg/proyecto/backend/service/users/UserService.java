package com.tfg.proyecto.backend.service.users;

import com.tfg.proyecto.backend.dto.request.auth.SignupRequest;
import com.tfg.proyecto.backend.dto.request.patient.PatientUpdateRequest;
import com.tfg.proyecto.backend.dto.response.patient.PatientListDetailsResponse;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.relation.RelationRepository;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import com.tfg.proyecto.backend.repository.users.UserRepository;
import com.tfg.proyecto.backend.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;
    private final RelationRepository relationRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, PatientRepository patientRepository, RelationRepository relationRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.patientRepository = patientRepository;
        this.relationRepository = relationRepository;
        this.jwtUtil = jwtUtil;
    }

    // -------------------- Crear usuario --------------------
    // Crea el usuario a partir de la request
    public User createUser(SignupRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setPhone(request.getPhone());

        User saved = userRepository.save(user); // lo guarda

        // Si es un paciente, entonces lo guarda también en la tabla Patients
        if (saved.getRole() == User.Role.PATIENT) {
            Patient patient = new Patient();
            patient.setUser(saved);
            patientRepository.save(patient);
        }

        return saved;
    }

    // -------------------- Obtener usuario por su id --------------------
    public User getUserById(Long id) {
        Optional<User> optional = userRepository.findById(id);

        if (optional.isEmpty()) {
            return null;
        }

        return optional.get();
    }

    // -------------------- Obtener detalles del paciente --------------------
    public PatientListDetailsResponse getPatientDetails(Long patientId, String authHeader) {
        String token = authHeader.substring(7); // extrae el usuario del token
        Long userId = Long.parseLong(jwtUtil.extractId(token));

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return null; // si no existe devuelve null
        }

        User user = userOptional.get();

        // comprueba que el usuario sea cuidador
        if (user.getRole() != User.Role.CAREGIVER) {
            return null; // si no devuelve null
        }

        // verifica que tengan una relación
        boolean linked = relationRepository.existsByCaregiverIdAndPatientId(userId, patientId);

        if (!linked) {
            return null; // si no están vinculados devuelve null
        }

        // obtiene al paciente
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);

        if (optionalPatient.isEmpty()) {
            return null; // si no existe devuelve null
        }

        Patient patient = optionalPatient.get(); // obtiene al paciente

        String username;
        String phone;

        // sí tiene una cuenta asociada
        if (patient.getUser() != null) {
            username = patient.getUser().getUsername();
            phone = patient.getUser().getPhone();
        } else { // si no la tiene
            username = patient.getName();
            phone = patient.getPhone();
        }

        // devuelve el DTO
        return new PatientListDetailsResponse(patient.getId(), username, phone, patient.getDiseaseStage(), patient.getNotes());
    }

    // -------------------- Actualizar paciente --------------------
    public boolean updatePatient(Long patientId, PatientUpdateRequest request, String authHeader) {
        String token = authHeader.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractId(token));  // extrae el id del usuario del token

        Optional<User> userOptional = userRepository.findById(userId); // busca el usuario

        if (userOptional.isEmpty()) {
            return false; // si no existe devuelve false
        }

        User selectedUser = userOptional.get(); // si existe obtiene el usuario

        // valida que el que edita sea un cuidador
        if (selectedUser.getRole() != User.Role.CAREGIVER) {
            return false;
        }

        // verifica que sea el cuidador del paciente
        boolean linked = relationRepository.existsByCaregiverIdAndPatientId(userId, patientId);

        if (!linked) {
            return false; // si no lo es devuelve false
        }

        // obtiene el paciente
        Optional<Patient> patientOpt = patientRepository.findById(patientId);

        if (patientOpt.isEmpty()) {
            return false; // si no existe devuelve false
        }

        Patient patient = patientOpt.get(); // obtiene el paciente
        User user = patient.getUser(); // obtiene la cuenta asociada

        // ---------- Paciente con cuenta ----------
        if (user != null) {
            if (request.getPhone() != null) user.setPhone(request.getPhone());
            if (request.getNotes() != null) patient.setNotes(request.getNotes());
        }
        // ---------- Paciente sin cuenta ----------
        else {
            if (request.getPhone() != null) patient.setPhone(request.getPhone());
            if (request.getNotes() != null) patient.setNotes(request.getNotes());
            if (request.getName() != null) patient.setName(request.getName());
        }

        // etapa de la enfermedad
        if (request.getDiseaseStage() != null) {
            patient.setDiseaseStage(request.getDiseaseStage());
        }

        // guardar cambios
        if (user != null) {
            userRepository.save(user);
        }
        patientRepository.save(patient);

        return true;
    }
}