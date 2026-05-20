package com.tfg.proyecto.backend.repository.appointment;

import com.tfg.proyecto.backend.model.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByPatientIdIn(List<Long> patientIds);
}