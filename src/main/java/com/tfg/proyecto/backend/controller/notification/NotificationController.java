package com.tfg.proyecto.backend.controller.notification;

import com.tfg.proyecto.backend.dto.response.notification.NotificationResponse;
import com.tfg.proyecto.backend.service.notification.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // Obtiene las notificaciones
    @GetMapping("/{patientId}")
    public List<NotificationResponse> getNotifications(@PathVariable Long patientId) {
        return service.getNotifications(patientId);
    }
}