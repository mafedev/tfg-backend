package com.tfg.proyecto.backend.service.routine;

import com.tfg.proyecto.backend.model.routine.Routine;
import com.tfg.proyecto.backend.model.routine.RoutineItem;
import com.tfg.proyecto.backend.model.routine.RoutineItemTracking;
import com.tfg.proyecto.backend.repository.routine.RoutineItemRepository;
import com.tfg.proyecto.backend.repository.routine.RoutineItemTrackingRepository;
import com.tfg.proyecto.backend.repository.routine.RoutineRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoutineItemTrackingService {

    private final RoutineItemTrackingRepository trackingRepository;
    private final RoutineItemRepository itemRepository;
    private final RoutineRepository routineRepository;

    public RoutineItemTrackingService(RoutineItemTrackingRepository trackingRepository, RoutineItemRepository itemRepository, RoutineRepository routineRepository) {
        this.trackingRepository = trackingRepository;
        this.itemRepository = itemRepository;
        this.routineRepository = routineRepository;
    }

    public RoutineItemTracking createTracking(Long itemId, LocalDate date) {
        RoutineItem item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item no encontrado"));

        return trackingRepository.findByRoutineItemIdAndDate(itemId, date).orElseGet(() -> {
            RoutineItemTracking tracking = new RoutineItemTracking();
            tracking.setRoutineItem(item);
            tracking.setDate(date);
            tracking.setScheduledTime(LocalDateTime.of(date, item.getTime()));
            tracking.setStatus(RoutineItemTracking.Status.PENDING);

            return trackingRepository.save(tracking);
        });
    }

    public void generateDailyTracking(Long patientId, LocalDate date) {
        Routine routine = routineRepository.findByPatientId(patientId).orElseThrow(() -> new RuntimeException("Rutina no encontrada"));
        List<RoutineItem> items = routine.getItems();

        for (RoutineItem item : items) {
            boolean exists = trackingRepository.existsByRoutineItemIdAndDate(item.getId(), date);
            if (!exists) {
                createTracking(item.getId(), date);
            }
        }
    }

    public List<RoutineItemTracking> getTrackingByPatientAndDate(Long patientId, LocalDate date) {

        Routine routine = routineRepository.findByPatientId(patientId).orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        return trackingRepository.findByRoutineItem_Routine_IdAndDate(routine.getId(), date);
    }

    public RoutineItemTracking updateStatus(Long trackingId, RoutineItemTracking.Status status) {
        RoutineItemTracking tracking = trackingRepository.findById(trackingId).orElseThrow(() -> new RuntimeException("Tracking no encontrado"));
        tracking.setStatus(status);

        if (status == RoutineItemTracking.Status.DONE) {
            tracking.setCompletedTime(LocalDateTime.now());
        }

        RoutineItemTracking saved = trackingRepository.save(tracking);
        return saved;
    }
}
