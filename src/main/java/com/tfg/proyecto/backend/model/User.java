package com.tfg.proyecto.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id // Indica que es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que el ID es autoincremental
    private Long id;

    @Column(nullable = false) // No puede estar vacío
    private String username;

    @Column(nullable = false, unique = true) // No puede estar vacío, no puede repetirse
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /*
     *
     * ********** Imagen del medicamento **********
     *
     */

    public enum Role {
        PATIENT,
        CAREGIVER
    }

    @Column(nullable = false)
    private String phone;

    // -------------------- Constructores --------------------
    public User() {
    }

    public User(Long id, String username, String email, String password, Role role, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
    }

    // -------------------- Getters y setters --------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

