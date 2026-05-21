package com.tfg.proyecto.backend.controller.home;

import com.tfg.proyecto.backend.dto.response.home.HomeAppointmentDetailResponse;
import com.tfg.proyecto.backend.dto.response.home.HomeCalendarResponse;
import com.tfg.proyecto.backend.dto.response.home.HomePendingTrackingResponse;
import com.tfg.proyecto.backend.dto.response.home.HomeTrackingResponse;
import com.tfg.proyecto.backend.security.SecurityUtils;
import com.tfg.proyecto.backend.service.home.CaregiverHomeService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/caregiver/home")
@CrossOrigin("*")
public class CaregiverHomeController {

    private final CaregiverHomeService service;
    private final SecurityUtils securityUtils;

    public CaregiverHomeController(CaregiverHomeService service, SecurityUtils securityUtils) {
        this.service = service;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/prescriptions")
    public List<HomeTrackingResponse> getHome() {

        Long caregiverId = securityUtils.getCurrentUserId();

        return service.getTodayTrackingByCaregiver(caregiverId);
    }

    @GetMapping("/calendar")
    public List<HomeCalendarResponse> getCalendar() {
        Long caregiverId = securityUtils.getCurrentUserId();
        return service.getAppointmentDays(caregiverId);
    }

    @GetMapping("/calendar/details")
    public List<HomeAppointmentDetailResponse> getDayAppointments(@RequestParam String date) {
        Long caregiverId = securityUtils.getCurrentUserId();
        return service.getAppointmentsByDate(caregiverId, LocalDate.parse(date));
    }

    @GetMapping("/pending")
    public List<HomePendingTrackingResponse> getPending() {
        Long caregiverId = securityUtils.getCurrentUserId();
        return service.getPendingToday(caregiverId);
    }
}