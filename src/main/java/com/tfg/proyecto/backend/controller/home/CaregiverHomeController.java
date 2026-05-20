package com.tfg.proyecto.backend.controller.home;

import com.tfg.proyecto.backend.dto.response.home.HomeAppointmentDetailResponse;
import com.tfg.proyecto.backend.dto.response.home.HomeCalendarResponse;
import com.tfg.proyecto.backend.dto.response.home.HomeTrackingResponse;
import com.tfg.proyecto.backend.security.JwtUtil;
import com.tfg.proyecto.backend.service.home.CaregiverHomeService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/caregiver/home")
@CrossOrigin("*")
public class CaregiverHomeController {

    private final CaregiverHomeService service;
    private final JwtUtil jwtUtil;

    public CaregiverHomeController(CaregiverHomeService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/prescriptions")
    public List<HomeTrackingResponse> getHome(@RequestHeader("Authorization") String authHeader) {

        Long caregiverId = extractId(authHeader);

        return service.getTodayTrackingByCaregiver(caregiverId);
    }

    private Long extractId(String authHeader) {
        String token = authHeader.substring(7);
        return Long.parseLong(jwtUtil.extractId(token));
    }

    @GetMapping("/calendar")
    public List<HomeCalendarResponse> getCalendar(@RequestHeader("Authorization") String authHeader) {
        Long caregiverId = extractId(authHeader);
        return service.getAppointmentDays(caregiverId);
    }

    @GetMapping("/calendar/details")
    public List<HomeAppointmentDetailResponse> getDayAppointments(@RequestHeader("Authorization") String authHeader, @RequestParam String date) {
        Long caregiverId = extractId(authHeader);
        return service.getAppointmentsByDate(caregiverId, LocalDate.parse(date));
    }
}