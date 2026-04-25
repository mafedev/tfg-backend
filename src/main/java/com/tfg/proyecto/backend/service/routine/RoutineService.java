package com.tfg.proyecto.backend.service.routine;

import com.tfg.proyecto.backend.dto.response.routine.RoutineItemResponse;
import com.tfg.proyecto.backend.dto.response.routine.RoutineResponse;
import com.tfg.proyecto.backend.model.routine.Routine;
import com.tfg.proyecto.backend.model.routine.RoutineItem;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.RelationRepository;
import com.tfg.proyecto.backend.repository.routine.RoutineRepository;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import com.tfg.proyecto.backend.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final PatientRepository patientRepository;
    private final RelationRepository relationRepository;
    private final JwtUtil jwtUtil;

    public RoutineService(RoutineRepository routineRepository, PatientRepository patientRepository, RelationRepository relationRepository, JwtUtil jwtUtil) {
        this.routineRepository = routineRepository;
        this.patientRepository = patientRepository;
        this.relationRepository = relationRepository;
        this.jwtUtil = jwtUtil;
    }

    // ------------- Crear una rutina -------------
    public RoutineResponse createRoutine(Long patientId, String authHeader) {
        Long caregiverId = extractId(authHeader);
        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return null;
        }

        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (routineRepository.findByPatientId(patientId).isPresent()) {
            return null;
        }

        Routine routine = new Routine();
        routine.setPatient(patient);

        Routine saved = routineRepository.save(routine);

        return mapToResponse(saved);
    }

    // ------------- Obtener rutina de un paciente (cuidador) -------------
    public RoutineResponse getRoutineAsCaregiver(Long patientId, String authHeader) {
        Long caregiverId = extractId(authHeader);

        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return null;
        }

        Routine routine = routineRepository.findByPatientId(patientId).orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        return mapToResponse(routine);
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

    private Long extractId(String authHeader) {
        String token = authHeader.substring(7);
        return Long.parseLong(jwtUtil.extractId(token));
    }
}
