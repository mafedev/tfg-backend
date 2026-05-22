package com.tfg.proyecto.backend.service.profile;

import com.tfg.proyecto.backend.dto.request.profile.ChangePasswordRequest;
import com.tfg.proyecto.backend.dto.request.profile.ProfileUpdateRequest;
import com.tfg.proyecto.backend.dto.response.profile.ProfileResponse;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.users.UserRepository;
import com.tfg.proyecto.backend.security.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder, SecurityUtils securityUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityUtils = securityUtils;
    }

    // ---------------- Obtener datos ----------------
    public ProfileResponse getProfile() {
        Long userId = securityUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return map(user);
    }

    // ---------------- Editar datos ----------------
    public ProfileResponse updateProfile(ProfileUpdateRequest request) {
        Long userId = securityUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // validar email único
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya existe");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        return map(userRepository.save(user));
    }

    // ---------------- Cambiar contraseña ----------------
    public void changePassword(ChangePasswordRequest request) {
        Long userId = securityUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private ProfileResponse map(User user) {
        return new ProfileResponse(user.getId(), user.getUsername(), user.getEmail(), user.getPhone());
    }
}