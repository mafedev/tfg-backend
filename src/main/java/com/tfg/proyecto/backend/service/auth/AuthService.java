package com.tfg.proyecto.backend.service.auth;

import com.tfg.proyecto.backend.dto.request.auth.LoginRequest;
import com.tfg.proyecto.backend.dto.request.auth.SignupRequest;
import com.tfg.proyecto.backend.dto.response.auth.LoginResponse;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.users.UserRepository;
import com.tfg.proyecto.backend.security.JwtUtil;
import com.tfg.proyecto.backend.service.users.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User signup(SignupRequest request) {
        return userService.createUser(request);
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return null; // el usuario no existe
        }

        User user = optionalUser.get();

        // comprueba la contraseña
        boolean passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatch) {
            return null; // contraseña incorrecta
        }

        // genera el token
        String token = jwtUtil.generateToken(user.getId());

        return new LoginResponse(user, token);
    }
}