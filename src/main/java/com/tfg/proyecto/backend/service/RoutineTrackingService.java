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

    // Obtiene el seguimiento
    @Transactional
    public RoutineTracking getTracking(Long patientId) {
        // busca la rutina
        Routine routine = routineRepository.findByPatientId(patientId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

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

    // Marca el desayuno como hecho con true
    @Transactional
    public RoutineTracking markBreakfast(Long patientId) {
        RoutineTracking tracking = getTracking(patientId);
        tracking.setBreakfastDone(!tracking.isBreakfastDone());
        return trackingRepository.save(tracking);
    }

    // Marca almuerzo
    @Transactional
    public RoutineTracking markLunch(Long patientId) {
        RoutineTracking tracking = getTracking(patientId);
        tracking.setLunchDone(!tracking.isLunchDone());
        return trackingRepository.save(tracking);
    }

    // Marca cena
    @Transactional
    public RoutineTracking markDinner(Long patientId) {
        RoutineTracking tracking = getTracking(patientId);
        tracking.setDinnerDone(!tracking.isDinnerDone());
        return trackingRepository.save(tracking);
    }

    // Marca baño
    @Transactional
    public RoutineTracking markBath(Long patientId) {
        RoutineTracking tracking = getTracking(patientId);
        tracking.setBathDone(!tracking.isBathDone());
        return trackingRepository.save(tracking);
    }

    // Marca hidratación
    @Transactional
    public RoutineTracking markHydration(Long patientId) {
        RoutineTracking tracking = getTracking(patientId);
        tracking.setHydrationDone(!tracking.isHydrationDone());
        return trackingRepository.save(tracking);
    }
}