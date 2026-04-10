package com.tfg.proyecto.backend.repository;

import com.tfg.proyecto.backend.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Todas las notificaciones del paciente
    List<Notification> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}