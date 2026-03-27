package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.model.routine.Routine;
import com.tfg.proyecto.backend.model.routine.RoutineTracking;
import com.tfg.proyecto.backend.repository.RoutineRepository;
import com.tfg.proyecto.backend.repository.RoutineTrackingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RoutineTrackingService {

    private final RoutineTrackingRepository trackingRepository;
    private final RoutineRepository routineRepository;

    public RoutineTrackingService(RoutineTrackingRepository trackingRepository, RoutineRepository routineRepository) {
        this.trackingRepository = trackingRepository;
        this.routineRepository = routineRepository;
    }

    // Obtiene o crea el seguimiento del día
    @Transactional
    public RoutineTracking getTracking(Long patientId) {
        Routine routine = routineRepository.findByPatientId(patientId).orElse(null);
        if (routine == null) {
            return null; // si no hay rutina, devuelve null
        }

        // obtiene fecha actual
        LocalDate today = LocalDate.now();

        // busca si existe el seguimiento
        RoutineTracking tracking = trackingRepository.findByRoutineIdAndDate(routine.getId(), today).orElse(null);

        if (tracking != null) {
            // si existe lo devuelve
            return tracking;
        } else {
            // si no existe lo crea
            RoutineTracking newTracking = new RoutineTracking();
            newTracking.setRoutine(routine);
            newTracking.setDate(today);

            // inicializa todo a false
            newTracking.setBreakfastDone(false);
            newTracking.setLunchDone(false);
            newTracking.setDinnerDone(false);
            newTracking.setBathDone(false);
            newTracking.setHydrationDone(false);

            // guarda el nuevo seguimiento y lo devuelve
            return trackingRepository.save(newTracking);
        }
    }

    // Actualiza cualquier actividad de la rutina
    @Transactional
    public RoutineTracking updateItem(Long patientId, String item, boolean done) {
        RoutineTracking tracking = getTracking(patientId);
        if (tracking == null) {
            return null; // no hace nada si no hay rutina
        }

        switch (item) {
            case "breakfast":
                tracking.setBreakfastDone(done);
                break;
            case "lunch":
                tracking.setLunchDone(done);
                break;
            case "dinner":
                tracking.setDinnerDone(done);
                break;
            case "bath":
                tracking.setBathDone(done);
                break;
            case "hydration":
                tracking.setHydrationDone(done);
                break;
            default: // si no existe no hace nada
                break;
        }
        return trackingRepository.save(tracking);
    }
}