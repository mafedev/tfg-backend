package com.tfg.proyecto.backend.controller;

import com.tfg.proyecto.backend.dto.request.MedicationScheduleRequest;
import com.tfg.proyecto.backend.model.medication.Medication;
import com.tfg.proyecto.backend.model.medication.MedicationSchedule;
import com.tfg.proyecto.backend.model.User;
import com.tfg.proyecto.backend.repository.MedicationRepository;
import com.tfg.proyecto.backend.repository.MedicationScheduleRepository;
import com.tfg.proyecto.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/medications/schedule")
@CrossOrigin("*")
public class MedicationScheduleController {

    // Repositorios
    private final UserRepository userRepository;
    private final MedicationRepository medicationRepository;
    private final MedicationScheduleRepository scheduleRepository;

    // Inyección de dependencias
    public MedicationScheduleController(UserRepository userRepository, MedicationRepository medicationRepository, MedicationScheduleRepository scheduleRepository) {
        this.userRepository = userRepository;
        this.medicationRepository = medicationRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // ------------ Crear nuevo horario de medicamento ------------
    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody MedicationScheduleRequest request) {
        // se busca al paciente por el id
        Optional<User> patientOptional = userRepository.findById(request.getPatientId());
        if (patientOptional.isEmpty()) { // si no existe, devuelve un 400
            return ResponseEntity.badRequest().body("Paciente no encontrado con ID: " + request.getPatientId());
        }

        // si el paciente existe, verifica si el medicamento existe
        Optional<Medication> medicationOptional = medicationRepository.findById(request.getMedicationId());
        if (medicationOptional.isEmpty()) { // si no existe, devuelve un 400
            return ResponseEntity.badRequest().body("Medicamento no encontrado con ID: " + request.getMedicationId());
        }

        // crea la rutina de medicamento
        MedicationSchedule schedule = new MedicationSchedule(
                patientOptional.get(),
                medicationOptional.get(),
                request.getDose(),
                request.getStartDate() != null ? LocalDate.parse(request.getStartDate()) : null,
                request.getEndDate() != null ? LocalDate.parse(request.getEndDate()) : null,
                request.getFrequencyHours(),
                request.getFirstDoseTime() != null ? LocalTime.parse(request.getFirstDoseTime()) : null,
                request.getDaysOfWeek()
        );

        // lo guarda
        scheduleRepository.save(schedule);

        // devuelve un 200
        return ResponseEntity.ok("Rutina de medicamento creada");
    }

    // ------------ Obtener medicación por paciente ------------
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getSchedulesByPatient(@PathVariable Long patientId) {

        // comprobar que el paciente existe
        Optional<User> patientOptional = userRepository.findById(patientId);
        if (patientOptional.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Paciente no encontrado con ID: " + patientId);
        }

        // obtener las rutinas
        var schedules = scheduleRepository.findByPatientId(patientId);

        return ResponseEntity.ok(schedules);
    }

    // ------------ Borrar medicación ------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long id, @RequestBody MedicationScheduleRequest request) {
        Optional<MedicationSchedule> optionalSchedule = scheduleRepository.findById(id);

        if (optionalSchedule.isEmpty()) {
            return ResponseEntity.badRequest().body("Rutina de medicamento no encontrada con ID: " + id);
        }

        MedicationSchedule schedule = optionalSchedule.get();

        schedule.setDose(request.getDose());
        schedule.setFrequencyHours(request.getFrequencyHours());
        schedule.setStartDate(request.getStartDate() != null ? LocalDate.parse(request.getStartDate()) : null);
        schedule.setEndDate(request.getEndDate() != null ? LocalDate.parse(request.getEndDate()) : null);
        schedule.setFirstDoseTime(request.getFirstDoseTime() != null ? LocalTime.parse(request.getFirstDoseTime()) : null);
        schedule.setDaysOfWeek(request.getDaysOfWeek());

        scheduleRepository.save(schedule);
        return ResponseEntity.ok(schedule);
    }
}
