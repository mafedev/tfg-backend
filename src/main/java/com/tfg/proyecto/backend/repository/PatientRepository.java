package com.tfg.proyecto.backend.repository;

import com.tfg.proyecto.backend.model.users.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Busca un paciente por el id del user asociado
    Optional<Patient> findByUserId(Long userId);
}