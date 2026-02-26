package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.dto.request.SignupRequest;
import com.tfg.proyecto.backend.dto.response.PatientListDetailsResponse;
import com.tfg.proyecto.backend.model.User;
import com.tfg.proyecto.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    // inyección de dependencias
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(SignupRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setPhone(request.getPhone());

        return userRepository.save(user);
    }

    public Optional<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }

    // Devuelve los datos de un paciente según su id
    public PatientListDetailsResponse getPatientDetails(Long patientId) {
        Optional<User> userRepo = userRepository.findById((patientId));

        if(userRepo.isPresent()){
            User user = userRepo.get();

            return new PatientListDetailsResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getPhone()
            );
        }
        return null;
    }
}
