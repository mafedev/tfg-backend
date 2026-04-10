package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.dto.request.AppointmentRequest;
import com.tfg.proyecto.backend.dto.response.AppointmentResponse;
import com.tfg.proyecto.backend.model.appointment.Appointment;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.AppointmentRepository;
import com.tfg.proyecto.backend.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    // Crear cita
    public Appointment createAppointment(AppointmentRequest request) {

        Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Appointment appointment = new Appointment();
        appointment.setTitle(request.getTitle());
        appointment.setDescription(request.getDescription());
        appointment.setLocation(request.getLocation());
        appointment.setDateTime(request.getDateTime());
        appointment.setPatient(patient);

        return appointmentRepository.save(appointment);
    }

    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.stream().map(a -> new AppointmentResponse(a.getId(), a.getTitle(), a.getDescription(), a.getLocation(), a.getDateTime())).toList();
    }
}
