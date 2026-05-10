package com.example.demo.dto;

import com.example.demo.model.Exercise;
import java.util.List;

public class MuscleGroupSuggestion {
    private String muscleGroup;
    private List<Exercise> exercises;

    public MuscleGroupSuggestion(String muscleGroup, List<Exercise> exercises) {
        this.muscleGroup = muscleGroup;
        this.exercises = exercises;
    }

    public String getMuscleGroup() { return muscleGroup; }
    public List<Exercise> getExercises() { return exercises; }
}
