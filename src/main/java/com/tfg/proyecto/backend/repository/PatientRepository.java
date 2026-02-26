package com.tfg.proyecto.backend.repository;

import com.tfg.proyecto.backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}