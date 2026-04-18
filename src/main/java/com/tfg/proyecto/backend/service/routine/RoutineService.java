package com.tfg.proyecto.backend.service.routine;

import com.tfg.proyecto.backend.dto.request.routine.RoutineRequest;
import com.tfg.proyecto.backend.model.routine.Routine;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.RelationRepository;
import com.tfg.proyecto.backend.repository.routine.RoutineRepository;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import com.tfg.proyecto.backend.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public Routine createRoutine(Long patientId, RoutineRequest request, String authHeader) {
        Long caregiverId = extractId(authHeader);
        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return null;
        }

        Optional<Patient> patientOptional = patientRepository.findById(patientId);

        if (patientOptional.isEmpty()) {
            return null;
        }

        if (routineRepository.findByPatientId(patientId).isPresent()) {
            return null;
        }

        Routine routine = new Routine(null, patientOptional.get(), request.getBreakfastTime(), request.getLunchTime(), request.getDinnerTime(), request.getBathTime(), request.isHydrationEnabled(), request.getHydrationReminderHours());
        return routineRepository.save(routine);
    }

    // ------------- Obtener rutina (paciente) -------------
    public Routine getOwnRoutine(String authHeader) {
        Long patientId = extractId(authHeader);
        return routineRepository.findByPatientId(patientId).orElse(null);
    }

    // ------------- Obtener rutina de un paciente (cuidador) -------------
    public Routine getRoutineAsCaregiver(Long patientId, String authHeader) {
        Long caregiverId = extractId(authHeader);

        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return null;
        }

        return routineRepository.findByPatientId(patientId).orElse(null);
    }

    // ------------- Editar una rutina -------------
    public Routine updateRoutine(Long patientId, RoutineRequest request, String authHeader) {
        Long caregiverId = extractId(authHeader);

        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return null;
        }

        Optional<Routine> optional = routineRepository.findByPatientId(patientId);

        if (optional.isEmpty()) {
            return null;
        }

        Routine routine = optional.get();
        routine.setBreakfastTime(request.getBreakfastTime());
        routine.setLunchTime(request.getLunchTime());
        routine.setDinnerTime(request.getDinnerTime());
        routine.setBathTime(request.getBathTime());
        routine.setHydrationEnabled(request.isHydrationEnabled());
        routine.setHydrationReminderHours(request.getHydrationReminderHours());

        return routineRepository.save(routine);
    }

    // ------------- Eliminar una rutina -------------
    public boolean deleteRoutine(Long patientId, String authHeader) {
        Long caregiverId = extractId(authHeader);
        boolean allowed = relationRepository.existsByCaregiverIdAndPatientId(caregiverId, patientId);

        if (!allowed) {
            return false;
        }

        Optional<Routine> optional = routineRepository.findByPatientId(patientId);

        if (optional.isEmpty()) {
            return false;
        }

        routineRepository.delete(optional.get());
        return true;
    }

    private Long extractId(String authHeader) {
        String token = authHeader.substring(7);
        return Long.parseLong(jwtUtil.extractId(token));
    }
}
