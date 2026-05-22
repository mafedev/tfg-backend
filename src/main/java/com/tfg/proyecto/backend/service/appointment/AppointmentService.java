package com.tfg.proyecto.backend.service.appointment;

import com.tfg.proyecto.backend.dto.request.appointment.AppointmentRequest;
import com.tfg.proyecto.backend.dto.response.appointment.AppointmentResponse;
import com.tfg.proyecto.backend.model.appointment.Appointment;
import com.tfg.proyecto.backend.model.users.Patient;
import com.tfg.proyecto.backend.repository.appointment.AppointmentRepository;
import com.tfg.proyecto.backend.repository.users.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    // -------------------- Crear cita --------------------
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        Optional<Patient> optionalPatient = patientRepository.findById(request.getPatientId());

        // si no existe el paciente
        if (optionalPatient.isEmpty()) {
            return null;
        }

        Patient patient = optionalPatient.get();

        Appointment appointment = new Appointment();
        appointment.setTitle(request.getTitle());
        appointment.setDescription(request.getDescription());
        appointment.setLocation(request.getLocation());
        appointment.setDateTime(request.getDateTime());
        appointment.setPatient(patient);

        Appointment saved = appointmentRepository.save(appointment);

        return mapToResponse(saved);
    }

    // -------------------- Obtener citas --------------------
    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId); // se obtienen las citas

        // lista donde se guarda la respuesta
        List<AppointmentResponse> responseList = new ArrayList<>();

        // itera cada cita
        for (Appointment a : appointments) {

            // se pasa a dto
            AppointmentResponse response = mapToResponse(a);

            // se agrega a la lista
            responseList.add(response);
        }

        return responseList;
    }

    // -------------------- Eliminar --------------------
    @Transactional
    public boolean deleteAppointment(Long id) {

        // se comprueba si existe
        boolean exists = appointmentRepository.existsById(id);

        if (!exists) {
            return false;
        }
        appointmentRepository.deleteById(id);
        return true;
    }

    // -------------------- Actualizar cita --------------------
    public AppointmentResponse updateAppointment(Long id, AppointmentRequest dto) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);

        // si no existe la cita
        if (optionalAppointment.isEmpty()) {
            return null;
        }

        Appointment appointment = optionalAppointment.get();

        // se actualizan los campos
        appointment.setTitle(dto.getTitle());
        appointment.setDescription(dto.getDescription());
        appointment.setLocation(dto.getLocation());
        appointment.setDateTime(dto.getDateTime());

        Appointment updated = appointmentRepository.save(appointment);

        return mapToResponse(updated);
    }

    // -------------------- Actualizar estado --------------------
    public boolean updateStatus(Long appointmentId, Appointment.AppointmentStatus status) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isEmpty()) {
            return false;
        }

        Appointment appointment = optionalAppointment.get();
        appointment.setStatus(status);
        appointmentRepository.save(appointment);

        return true;
    }

    private AppointmentResponse mapToResponse(Appointment a) {
        return new AppointmentResponse(a.getId(), a.getTitle(), a.getDescription(), a.getLocation(), a.getDateTime(), a.getStatus().name());
    }

    // -------------------- Obtener citas paciente --------------------
    public List<AppointmentResponse> getAppointmentsByUser(Long userId) {
        Optional<Patient> optionalPatient = patientRepository.findByUserId(userId);

        if (optionalPatient.isEmpty()) {
            return new ArrayList<>();
        }

        Patient patient = optionalPatient.get();
        List<Appointment> appointments = appointmentRepository.findByPatientId(patient.getId());
        List<AppointmentResponse> responseList = new ArrayList<>();

        for (Appointment a : appointments) {
            if (a.getStatus() != Appointment.AppointmentStatus.PENDING) {
                continue;
            }

            responseList.add(mapToResponse(a));
        }

        return responseList;
    }
}
