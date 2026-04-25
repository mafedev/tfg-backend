package com.tfg.proyecto.backend.model.routine;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "routine_items")
public class RoutineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cada item pertenece a una rutina
    @ManyToOne
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    private String name;
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    public enum ItemType {
        MEAL, HYGIENE, HEALTH, OTHER
    }

    // -------------------- Constructores --------------------
    public RoutineItem() {
    }

    public RoutineItem(Long id, Routine routine, String name, LocalTime time, ItemType type) {
        this.id = id;
        this.routine = routine;
        this.name = name;
        this.time = time;
        this.type = type;
    }

    // -------------------- Getters y Setters --------------------
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }
}
