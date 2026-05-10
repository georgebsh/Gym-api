package com.example.demo.dto;

public class ScheduleRequest {
    private String dayOfWeek;
    private String gymTime;
    private String muscleGroup;
    private String notes;

    public String getDayOfWeek() { return dayOfWeek; }
    public String getGymTime() { return gymTime; }
    public String getMuscleGroup() { return muscleGroup; }
    public String getNotes() { return notes; }

    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public void setGymTime(String gymTime) { this.gymTime = gymTime; }
    public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }
    public void setNotes(String notes) { this.notes = notes; }
}
