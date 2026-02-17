package com.tfg.proyecto.backend.model.routine;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "routines_tracking")
public class RoutineTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    // Día
    private LocalDate date;

    // si lo hizo o no
    private boolean breakfastDone;
    private boolean lunchDone;
    private boolean dinnerDone;
    private boolean bathDone;
    private boolean hydrationDone;

    // Constructores

    public RoutineTracking(Long id, Routine routine, LocalDate date, boolean breakfastDone, boolean lunchDone, boolean dinnerDone, boolean bathDone, boolean hydrationDone) {
        this.id = id;
        this.routine = routine;
        this.date = date;
        this.breakfastDone = breakfastDone;
        this.lunchDone = lunchDone;
        this.dinnerDone = dinnerDone;
        this.bathDone = bathDone;
        this.hydrationDone = hydrationDone;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Routine getRoutine() {
        return routine;
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isBreakfastDone() {
        return breakfastDone;
    }

    public void setBreakfastDone(boolean breakfastDone) {
        this.breakfastDone = breakfastDone;
    }

    public boolean isLunchDone() {
        return lunchDone;
    }

    public void setLunchDone(boolean lunchDone) {
        this.lunchDone = lunchDone;
    }

    public boolean isDinnerDone() {
        return dinnerDone;
    }

    public void setDinnerDone(boolean dinnerDone) {
        this.dinnerDone = dinnerDone;
    }

    public boolean isBathDone() {
        return bathDone;
    }

    public void setBathDone(boolean bathDone) {
        this.bathDone = bathDone;
    }

    public boolean isHydrationDone() {
        return hydrationDone;
    }

    public void setHydrationDone(boolean hydrationDone) {
        this.hydrationDone = hydrationDone;
    }
}
