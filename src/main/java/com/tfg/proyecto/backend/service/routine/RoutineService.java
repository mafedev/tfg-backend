package com.tfg.proyecto.backend.service.routine;

import com.tfg.proyecto.backend.dto.response.routine.RoutineItemResponse;
import com.tfg.proyecto.backend.dto.response.routine.RoutineResponse;
import com.tfg.proyecto.backend.model.routine.Routine;
import com.tfg.proyecto.backend.model.routine.RoutineItem;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.relation.RelationRepository;
import com.tfg.proyecto.backend.repository.routine.RoutineRepository;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import com.tfg.proyecto.backend.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final PatientRepository patientRepository;
    private final RelationRepository relationRepository;
    private final SecurityUtils securityUtils;

    public RoutineService(RoutineRepository routineRepository, PatientRepository patientRepository, RelationRepository relationRepository, SecurityUtils securityUtils) {
        this.routineRepository = routineRepository;
        this.patientRepository = patientRepository;
        this.relationRepository = relationRepository;
        this.securityUtils = securityUtils;
    }

    // ------------- Crear una rutina -------------
    public RoutineResponse createRoutine(Long patientId) {
        Long caregiverId = securityUtils.getCurrentUserId();
        validateRelation(caregiverId, patientId);

        Patient patient = patientRepository.findById(patientId).orElse(null);

        if (patient == null) {
            return null;
        }

        if (routineRepository.findByPatientId(patientId).isPresent()) {
            return null;
        }

        Routine routine = new Routine();
        routine.setPatient(patient);

        Routine saved = routineRepository.save(routine);

        return mapToResponse(saved);
    }

    // ------------- Obtener rutina de un paciente (cuidador) -------------
    public RoutineResponse getRoutineAsCaregiver(Long patientId) {
        Long caregiverId = securityUtils.getCurrentUserId();
        validateRelation(caregiverId, patientId);

        Routine routine = routineRepository.findByPatientId(patientId).orElse(null);

        if (routine == null) {
            return null;
        }

        return mapToResponse(routine);
    }

    // ---------------- Eliminar rutina ----------------
    public void deleteRoutine(Long patientId) {
        Long caregiverId = securityUtils.getCurrentUserId();
        validateRelation(caregiverId, patientId);

        Routine routine = routineRepository.findByPatientId(patientId).orElse(null);

        if (routine == null) {
            return;
        }

        routineRepository.delete(routine);
    }

    private RoutineResponse mapToResponse(Routine routine) {
        RoutineResponse res = new RoutineResponse();
        res.setId(routine.getId());
        res.setPatientId(routine.getPatient().getId());

        List<RoutineItemResponse> items = routine.getItems().stream().map(this::mapItem).toList();

        res.setItems(items);

        return res;
    }

    private RoutineItemResponse mapItem(RoutineItem item) {

        RoutineItemResponse dto = new RoutineItemResponse();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setTime(item.getTime());
        dto.setType(item.getType().name());

        return dto;
    }

    private void validateRelation(Long caregiverId, Long patientId) {
        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiverId, patientId);
        if (!allowed) {
            return;
        }
    }

    public RoutineResponse getRoutineAsPatient() {
        Long userId = securityUtils.getCurrentUserId();
        Patient patient = patientRepository.findByUserId(userId).orElse(null);

        if (patient == null) {
            return null;
        }

        Routine routine = routineRepository.findByPatientId(patient.getId()).orElse(null);

        if (routine == null) {
            return null;
        }

        return mapToResponse(routine);
    }
}