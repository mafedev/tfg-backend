package com.tfg.proyecto.backend.service.medication;

import com.tfg.proyecto.backend.dto.request.medication.MedicationRequest;
import com.tfg.proyecto.backend.model.medication.Medication;
import com.tfg.proyecto.backend.repository.medication.MedicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    // -------------------- Crear medicamento --------------------
    public Medication createMedication(MedicationRequest request) {
        Medication medication = new Medication(request.getName(), request.getDescription(), request.getType());
        return medicationRepository.save(medication);
    }

    // -------------------- Obtener todos los medicamentos --------------------
    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }
}
