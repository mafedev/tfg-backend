package com.tfg.proyecto.backend.repository.allergies;

import com.tfg.proyecto.backend.model.allergies.PatientAllergy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientAllergyRepository extends JpaRepository<PatientAllergy, Long> {
    List<PatientAllergy> findByPatientId(Long patientId);
    void deleteByPatientIdAndAllergyId(Long patientId, Long allergyId);
    Optional<PatientAllergy> findByPatientIdAndAllergyId(Long patientId, Long allergyId);
}