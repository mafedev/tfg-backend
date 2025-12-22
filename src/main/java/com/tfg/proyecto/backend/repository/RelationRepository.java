package com.tfg.proyecto.backend.repository;

import com.tfg.proyecto.backend.entity_model.Relation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RelationRepository extends JpaRepository<Relation, Long> { // indica el tipo de objeto que maneja y el tipo de la clave primaria
    boolean existsByCode(String code);
    Optional<Relation> findByCode(String code);
    boolean existsByCaregiverIdAndPatientId(Long caregiverId, Long patientId);
}
