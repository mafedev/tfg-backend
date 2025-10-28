package com.tfg.proyecto.backend.dto;

public class RutineRequest {

    private String breakfastTime;
    private String lunchTime;
    private String dinnerTime;
    private String bathTime;
    private Boolean enableHydration;
    private Integer hydrationIntervalHours;

    public RutineRequest() {
    }

    public RutineRequest(String breakfastTime, String lunchTime, String dinnerTime, String bathTime, Boolean enableHydration, Integer hydrationIntervalHours) {
        this.breakfastTime = breakfastTime;
        this.lunchTime = lunchTime;
        this.dinnerTime = dinnerTime;
        this.bathTime = bathTime;
        this.enableHydration = enableHydration;
        this.hydrationIntervalHours = hydrationIntervalHours;
    }

    public String getBreakfastTime() {
        return breakfastTime;
    }

    public void setBreakfastTime(String breakfastTime) {
        this.breakfastTime = breakfastTime;
    }

    public String getLunchTime() {
        return lunchTime;
    }

    public void setLunchTime(String lunchTime) {
        this.lunchTime = lunchTime;
    }

    public String getDinnerTime() {
        return dinnerTime;
    }

    public void setDinnerTime(String dinnerTime) {
        this.dinnerTime = dinnerTime;
    }

    public String getBathTime() {
        return bathTime;
    }

    public void setBathTime(String bathTime) {
        this.bathTime = bathTime;
    }

    public Boolean getEnableHydration() {
        return enableHydration;
    }

    public void setEnableHydration(Boolean enableHydration) {
        this.enableHydration = enableHydration;
    }

    public Integer getHydrationIntervalHours() {
        return hydrationIntervalHours;
    }

    public void setHydrationIntervalHours(Integer hydrationIntervalHours) {
        this.hydrationIntervalHours = hydrationIntervalHours;
    }
}
