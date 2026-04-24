package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.dto.response.NotificationResponse;
import com.tfg.proyecto.backend.model.notification.Notification;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.model.users.User;
import com.tfg.proyecto.backend.repository.NotificationRepository;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);

        if (optionalPatient.isEmpty()) {
            // Si no existe, simplemente no hacemos nada (o podrías loggear)
            return;
        }

        Patient patient = optionalPatient.get();

        // Obtenemos el usuario asociado al paciente (puede ser null)
        User user = patient.getUser();

        // Verifica el rol, y solo la crea si es paciente
        if (user != null && user.getRole() == User.Role.PATIENT) {
            Notification notification = new Notification();
            notification.setPatient(patient); // se asocia la notificación
            notification.setTitle(title);
            notification.setMessage(message);

            notificationRepository.save(notification); // la guarda
        }
    }

    // Obtiene todas las notificaciones de un paciente
    public List<NotificationResponse> getNotifications(Long patientId) {
        return notificationRepository.findByPatientIdOrderByCreatedAtDesc(patientId)
                .stream() // se convierte cada entidad en DTO
                .map(n -> new NotificationResponse(
                        n.getId(), // id de la notificación
                        n.getTitle(),
                        n.getMessage(),
                        n.getCreatedAt()
                ))
                // se convierte el stream en una lista
                .collect(Collectors.toList());
    }
}