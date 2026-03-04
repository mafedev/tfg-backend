package com.tfg.proyecto.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.tfg.proyecto.backend.model.User.Role;

// Datos que envia el front para crear un usuario
public class SignupRequest {

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener mínimo 6 caracteres")
    private String password;

    @NotBlank(message = "El rol es obligatorio")

    @NotNull
    private Role role;

    @NotBlank(message = "El teléfono es obligatorio")
    private String phone;

    // -------------------- Constructor --------------------
    public SignupRequest() {
    }

    // -------------------- Getters y Setters --------------------
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
