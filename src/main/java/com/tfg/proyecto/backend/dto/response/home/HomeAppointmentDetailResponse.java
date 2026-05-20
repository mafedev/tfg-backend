package com.tfg.proyecto.backend.dto.response.home;

public class HomeAppointmentDetailResponse {

    private String title;
    private String patientName;
    private String location;
    private String dateTime;

    public HomeAppointmentDetailResponse() {
    }

    public HomeAppointmentDetailResponse(String title, String patientName, String location, String dateTime) {
        this.title = title;
        this.patientName = patientName;
        this.location = location;
        this.dateTime = dateTime;
    }

    // getters/setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}