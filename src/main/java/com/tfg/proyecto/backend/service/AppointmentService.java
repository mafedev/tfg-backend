package com.tfg.proyecto.backend.service;

import com.tfg.proyecto.backend.dto.request.AppointmentRequest;
import com.tfg.proyecto.backend.dto.response.AppointmentResponse;
import com.tfg.proyecto.backend.model.appointment.Appointment;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.AppointmentRepository;
import com.tfg.proyecto.backend.repository.PatientRepository;
import jakarta.transaction.Transactional;
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
        return appointments.stream().map(a -> new AppointmentResponse(a.getId(), a.getTitle(), a.getDescription(), a.getLocation(), a.getDateTime(), a.getStatus().name())).toList();
    }

    // borrar
    @Transactional
    public boolean deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            return false;
        }
        appointmentRepository.deleteById(id);
        return true;
    }

    public Appointment updateAppointment(Long id, AppointmentRequest dto) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        appointment.setTitle(dto.getTitle());
        appointment.setDescription(dto.getDescription());
        appointment.setLocation(dto.getLocation());
        appointment.setDateTime(dto.getDateTime());

        return appointmentRepository.save(appointment);
    }

    public void updateStatus(Long appointmentId, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
    }
}
