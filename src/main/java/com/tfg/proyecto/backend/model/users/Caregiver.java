package com.tfg.proyecto.backend.model.users;

import jakarta.persistence.*;

@Entity
@Table(name = "caregivers")
public class Caregiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // -------------------- Constructores --------------------
    public Caregiver() {
    }

    public Caregiver(Long id, String name, String phone, User user) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.user = user;
    }

    // -------------------- Getters y Setters --------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
