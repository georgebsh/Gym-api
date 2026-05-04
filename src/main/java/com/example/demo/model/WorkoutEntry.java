package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "workout_entries")
public class WorkoutEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    private int sets;
    private int reps;
    private double weight;

    public WorkoutEntry() {}

    public Long getId() { return id; }
    public Workout getWorkout() { return workout; }
    public Exercise getExercise() { return exercise; }
    public int getSets() { return sets; }
    public int getReps() { return reps; }
    public double getWeight() { return weight; }

    public void setId(Long id) { this.id = id; }
    public void setWorkout(Workout workout) { this.workout = workout; }
    public void setExercise(Exercise exercise) { this.exercise = exercise; }
    public void setSets(int sets) { this.sets = sets; }
    public void setReps(int reps) { this.reps = reps; }
    public void setWeight(double weight) { this.weight = weight; }
}
