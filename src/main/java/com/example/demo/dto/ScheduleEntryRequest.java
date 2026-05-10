package com.example.demo.dto;

public class ScheduleEntryRequest {
    private Long exerciseId;
    private int sets;
    private int reps;

    public Long getExerciseId() { return exerciseId; }
    public int getSets() { return sets; }
    public int getReps() { return reps; }

    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }
    public void setSets(int sets) { this.sets = sets; }
    public void setReps(int reps) { this.reps = reps; }
}
