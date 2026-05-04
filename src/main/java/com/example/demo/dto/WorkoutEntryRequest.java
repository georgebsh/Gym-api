package com.example.demo.dto;

public class WorkoutEntryRequest {
    private Long exerciseId;
    private int sets;
    private int reps;
    private double weight;

    public Long getExerciseId() { return exerciseId; }
    public int getSets() { return sets; }
    public int getReps() { return reps; }
    public double getWeight() { return weight; }

    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }
    public void setSets(int sets) { this.sets = sets; }
    public void setReps(int reps) { this.reps = reps; }
    public void setWeight(double weight) { this.weight = weight; }
}
