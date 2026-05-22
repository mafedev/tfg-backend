package com.tfg.proyecto.backend.controller.home;

import com.tfg.proyecto.backend.dto.response.home.*;
import com.tfg.proyecto.backend.service.home.CaregiverHomeService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/caregiver/home")
@CrossOrigin("*")
public class CaregiverHomeController {

    private final CaregiverHomeService service;

    public CaregiverHomeController(CaregiverHomeService service) {
        this.service = service;
    }

    @GetMapping("/prescriptions")
    public List<HomeTrackingResponse> getHome() {
        return service.getTodayTrackingByCaregiver();
    }

    @GetMapping("/calendar")
    public List<HomeCalendarResponse> getCalendar() {
        return service.getAppointmentDays();
    }

    @GetMapping("/calendar/details")
    public List<HomeAppointmentDetailResponse> getDayAppointments(@RequestParam String date) {
        return service.getAppointmentsByDate(LocalDate.parse(date));
    }

    @GetMapping("/pending")
    public List<HomePendingTrackingResponse> getPending() {
        return service.getPendingToday();
    }
}