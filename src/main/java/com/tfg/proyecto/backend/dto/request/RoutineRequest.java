package com.tfg.proyecto.backend.dto.request;

public class RoutineRequest {
    private Long patientId;
    private String breakfastTime;
    private String lunchTime;
    private String dinnerTime;
    private String bathTime;
    private Boolean enableHydration;
    private Integer waterReminder;

    // Getters y setters
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getBreakfastTime() { return breakfastTime; }
    public void setBreakfastTime(String breakfastTime) { this.breakfastTime = breakfastTime; }

    public String getLunchTime() { return lunchTime; }
    public void setLunchTime(String lunchTime) { this.lunchTime = lunchTime; }

    public String getDinnerTime() { return dinnerTime; }
    public void setDinnerTime(String dinnerTime) { this.dinnerTime = dinnerTime; }

    public String getBathTime() { return bathTime; }
    public void setBathTime(String bathTime) { this.bathTime = bathTime; }

    public Boolean getEnableHydration() { return enableHydration; }
    public void setEnableHydration(Boolean enableHydration) { this.enableHydration = enableHydration; }

    public Integer getWaterReminder() { return waterReminder; }
    public void setWaterReminder(Integer waterReminder) { this.waterReminder = waterReminder; }
}
