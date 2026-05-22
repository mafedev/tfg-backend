package com.tfg.proyecto.backend.controller.profile;

import com.tfg.proyecto.backend.dto.request.profile.ChangePasswordRequest;
import com.tfg.proyecto.backend.dto.request.profile.ProfileUpdateRequest;
import com.tfg.proyecto.backend.dto.response.profile.ProfileResponse;
import com.tfg.proyecto.backend.service.profile.ProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // ---------------- Obtener datos ----------------
    @GetMapping("/me")
    public ProfileResponse getMyProfile() {
        return profileService.getProfile();
    }

    // ---------------- Actualizar datos ----------------
    @PutMapping("/me")
    public ProfileResponse updateMyProfile(@RequestBody ProfileUpdateRequest request) {
        return profileService.updateProfile(request);
    }

    // ---------------- Cambiar contraseña ----------------
    @PutMapping("/me/password")
    public void changePassword(@RequestBody ChangePasswordRequest request) {
        profileService.changePassword(request);
    }
}