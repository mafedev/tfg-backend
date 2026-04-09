package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.model.notification.Notification;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.NotificationRepository;
import com.tfg.proyecto.backend.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PatientRepository patientRepository;

    public NotificationService(NotificationRepository notificationRepository, PatientRepository patientRepository) {
        this.notificationRepository = notificationRepository;
        this.patientRepository = patientRepository;
    }

    // Crea la notificación solo para los pacientes
    public void createNotification(Long patientId, String title, String message) {
        // Busca al paciente por el id
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Obtiene el usuario relacionado
        User user = patient.getUser();

        // Verifica el rol, y solo la crea si es paciente
        if (user != null && user.getRole() == User.Role.PATIENT) {

            Notification notification = new Notification();
            notification.setPatientId(patientId);
            notification.setTitle(title);
            notification.setMessage(message);

            notificationRepository.save(notification); // la guarda
        }
    }

    // Obtiene todas las notificaciones de un paciente
    public List<com.tfg.proyecto.backend.dto.response.NotificationResponse> getNotifications(Long patientId) {
        return notificationRepository.findByPatientIdOrderByCreatedAtDesc(patientId).stream().map(n -> new com.tfg.proyecto.backend.dto.response.NotificationResponse(n.getId(), n.getTitle(), n.getMessage(), n.isRead(), n.getCreatedAt())).collect(Collectors.toList());
    }
}