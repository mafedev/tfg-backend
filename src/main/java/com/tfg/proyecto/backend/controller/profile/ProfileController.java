package com.tfg.proyecto.backend.controller.profile;

import com.tfg.proyecto.backend.dto.request.profile.ProfileUpdateRequest;
import com.tfg.proyecto.backend.dto.response.profile.ProfileResponse;
import com.tfg.proyecto.backend.security.JwtUtil;
import com.tfg.proyecto.backend.service.profile.ProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class ProfileController {

    private final ProfileService profileService;
    private final JwtUtil jwtUtil;

    public ProfileController(ProfileService profileService, JwtUtil jwtUtil) {
        this.profileService = profileService;
        this.jwtUtil = jwtUtil;
    }

    // ---------------- Obtener datos ----------------
    @GetMapping("/me")
    public ProfileResponse getMyProfile(@RequestHeader("Authorization") String authHeader) {

        Long userId = extractId(authHeader);

        return profileService.getProfile(userId);
    }

    // ---------------- Actualizar datos ----------------
    @PutMapping("/me")
    public ProfileResponse updateMyProfile(@RequestHeader("Authorization") String authHeader, @RequestBody ProfileUpdateRequest request) {

        Long userId = extractId(authHeader);

        return profileService.updateProfile(userId, request);
    }

    private Long extractId(String authHeader) {
        String token = authHeader.substring(7);
        return Long.parseLong(jwtUtil.extractId(token));
    }
}