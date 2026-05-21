package com.tfg.proyecto.backend.service.routine;

import com.tfg.proyecto.backend.dto.response.routine.RoutineItemTrackingResponse;
import com.tfg.proyecto.backend.model.routine.Routine;
import com.tfg.proyecto.backend.model.routine.RoutineItem;
import com.tfg.proyecto.backend.model.routine.RoutineItemTracking;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.routine.RoutineItemRepository;
import com.tfg.proyecto.backend.repository.routine.RoutineItemTrackingRepository;
import com.tfg.proyecto.backend.repository.routine.RoutineRepository;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoutineItemTrackingService {

    private final RoutineItemTrackingRepository trackingRepository;
    private final RoutineItemRepository itemRepository;
    private final RoutineRepository routineRepository;
    private final PatientRepository patientRepository;

    public RoutineItemTrackingService(RoutineItemTrackingRepository trackingRepository, RoutineItemRepository itemRepository, RoutineRepository routineRepository, PatientRepository patientRepository) {
        this.trackingRepository = trackingRepository;
        this.itemRepository = itemRepository;
        this.routineRepository = routineRepository;
        this.patientRepository = patientRepository;
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

        return trackingRepository.save(tracking);
    }

    public List<RoutineItemTrackingResponse> getTrackingForPatient(String date) {
        Long userId = Long.parseLong( SecurityContextHolder.getContext().getAuthentication().getName());

        Patient patient = patientRepository.findByUserId(userId).orElse(null);

        if (patient == null) {
            return null;
        }

        LocalDate parsedDate = LocalDate.parse(date);

        if (parsedDate.equals(LocalDate.now())) {
            generateDailyTracking(patient.getId(), parsedDate);
        }

        return getTrackingByPatientAndDate(patient.getId(), parsedDate).stream().map(RoutineItemTrackingResponse::new).toList();
    }
}