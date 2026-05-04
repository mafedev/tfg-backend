package com.tfg.proyecto.backend.repository.allergies;

import com.tfg.proyecto.backend.model.allergies.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AllergyRepository extends JpaRepository<Allergy, Long> {
    Optional<Allergy> findByNameIgnoreCase(String name);
}