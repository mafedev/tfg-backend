package com.tfg.proyecto.backend.repository;

import com.tfg.proyecto.backend.model.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}