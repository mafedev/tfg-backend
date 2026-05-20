package com.tfg.proyecto.backend.service.profile;

import com.tfg.proyecto.backend.dto.request.profile.ProfileUpdateRequest;
import com.tfg.proyecto.backend.dto.response.profile.ProfileResponse;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.users.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ---------------- Obtener datos ----------------
    public ProfileResponse getProfile(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return map(user);
    }

    // ---------------- Editar datos ----------------
    public ProfileResponse updateProfile(Long userId, ProfileUpdateRequest request) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // validar email único
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya existe");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        User saved = userRepository.save(user);

        return map(saved);
    }

    private ProfileResponse map(User user) {
        return new ProfileResponse(user.getId(), user.getUsername(), user.getEmail(), user.getPhone());
    }
}