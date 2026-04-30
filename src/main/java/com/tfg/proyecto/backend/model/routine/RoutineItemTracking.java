package com.tfg.proyecto.backend.model.routine;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "routine_item_tracking", uniqueConstraints = {@UniqueConstraint(columnNames = {"routine_item_id", "date"})})
public class RoutineItemTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "routine_item_id", nullable = false)
    private RoutineItem routineItem;

    private LocalDate date;

    private LocalDateTime scheduledTime;
    private LocalDateTime completedTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING, DONE, SKIPPED
    }

    // -------------------- Constructores --------------------
    public RoutineItemTracking() {
    }

    public RoutineItemTracking(Long id, RoutineItem routineItem, LocalDate date, LocalDateTime scheduledTime, LocalDateTime completedTime, Status status) {
        this.id = id;
        this.routineItem = routineItem;
        this.date = date;
        this.scheduledTime = scheduledTime;
        this.completedTime = completedTime;
        this.status = status;
    }

    // -------------------- Getters y Setters --------------------
    public Long getId() {
        return id;
    }

    public RoutineItem getRoutineItem() {
        return routineItem;
    }

    public void setRoutineItem(RoutineItem routineItem) {
        this.routineItem = routineItem;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public LocalDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
