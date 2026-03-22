package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.dto.request.SignupRequest;
import com.tfg.proyecto.backend.dto.response.PatientListDetailsResponse;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.PatientRepository;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    // inyección de dependencias
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, PatientRepository patientRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.patientRepository = patientRepository;
    }

    public User registerUser(SignupRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setPhone(request.getPhone());

        User newUser = userRepository.save(user); // lo guarda

        // Si es un paciente, entonces lo guarda también en la tabla Patients
        if(newUser.getRole() == User.Role.PATIENT){
            Patient patient = new Patient();
            patient.setUser(newUser);
            patientRepository.save(patient);
        }

        return newUser;
    }

    public Optional<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }

    // Devuelve los datos de un paciente según su id
    public PatientListDetailsResponse getPatientDetails(Long patientId) {
        Optional<Patient> patientRepo = patientRepository.findById(patientId);

        if(patientRepo.isPresent()){
            Patient patient = patientRepo.get();
            User user = patient.getUser();

            return new PatientListDetailsResponse(
                    patient.getId(),
                    user.getUsername(),
                    user.getPhone(),
                    patient.getDiseaseStage(),
                    patient.getNotes()
            );
        }
        return null;
    }
}
