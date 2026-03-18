package com.tfg.proyecto.backend.repository;

import com.tfg.proyecto.backend.model.users.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {

    Optional<Caregiver> findByUserId(Long userId);
}