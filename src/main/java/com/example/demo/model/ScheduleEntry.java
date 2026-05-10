package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "schedule_entries")
public class ScheduleEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private WorkoutSchedule schedule;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    private int sets;

    @Column(nullable = false)
    private int reps;

    public ScheduleEntry() {}

    public Long getId() { return id; }
    public WorkoutSchedule getSchedule() { return schedule; }
    public Exercise getExercise() { return exercise; }
    public int getSets() { return sets; }
    public int getReps() { return reps; }

    public void setId(Long id) { this.id = id; }
    public void setSchedule(WorkoutSchedule schedule) { this.schedule = schedule; }
    public void setExercise(Exercise exercise) { this.exercise = exercise; }
    public void setSets(int sets) { this.sets = sets; }
    public void setReps(int reps) { this.reps = reps; }
}
