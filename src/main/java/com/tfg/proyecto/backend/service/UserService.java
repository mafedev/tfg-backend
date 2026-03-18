package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.dto.request.SignupRequest;
import com.tfg.proyecto.backend.dto.response.PatientListDetailsResponse;
import com.tfg.proyecto.backend.model.users.Caregiver;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.CaregiverRepository;
import com.tfg.proyecto.backend.repository.PatientRepository;
import com.tfg.proyecto.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;
    private final CaregiverRepository caregiverRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                       PatientRepository patientRepository, CaregiverRepository caregiverRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.patientRepository = patientRepository;
        this.caregiverRepository = caregiverRepository;
    }

    // -------------------- Registro --------------------
    public User registerUser(SignupRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User newUser = userRepository.save(user);

        if (newUser.getRole() == User.Role.PATIENT) {
            Patient patient = new Patient();
            patient.setUser(newUser);
            patient.setName(request.getName());
            patient.setPhone(request.getPhone());
            patientRepository.save(patient);
        } else if (newUser.getRole() == User.Role.CAREGIVER) {
            Caregiver caregiver = new Caregiver();
            caregiver.setUser(newUser);
            caregiver.setName(request.getName());
            caregiver.setPhone(request.getPhone());
            caregiverRepository.save(caregiver);
        }

        return newUser;
    }

    // -------------------- Login --------------------
    public Optional<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }

    // -------------------- Obtener paciente por userId --------------------
    public Optional<Patient> getPatientByUserId(Long userId) {
        return patientRepository.findByUserId(userId);
    }

    // -------------------- Obtener cuidador por userId --------------------
    public Optional<Caregiver> getCaregiverByUserId(Long userId) {
        return caregiverRepository.findByUserId(userId);
    }

    // -------------------- Obtener detalles de un paciente --------------------
    public PatientListDetailsResponse getPatientDetails(Long patientId) {
        Optional<Patient> patientOpt = patientRepository.findById(patientId);

        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            User user = patient.getUser();

            return new PatientListDetailsResponse(
                    user != null ? user.getId() : null,
                    user != null ? user.getUsername() : null,
                    patient.getPhone(),
                    patient.getDiseaseStage(),
                    patient.getNotes()
            );
        }

        return null;
    }

    // -------------------- Helpers opcionales --------------------
    public String getPatientNameByUser(Long userId) {
        return getPatientByUserId(userId).map(Patient::getName).orElse(null);
    }

    public String getPatientPhoneByUser(Long userId) {
        return getPatientByUserId(userId).map(Patient::getPhone).orElse(null);
    }

    public String getCaregiverNameByUser(Long userId) {
        return getCaregiverByUserId(userId).map(Caregiver::getName).orElse(null);
    }

    public String getCaregiverPhoneByUser(Long userId) {
        return getCaregiverByUserId(userId).map(Caregiver::getPhone).orElse(null);
    }
}