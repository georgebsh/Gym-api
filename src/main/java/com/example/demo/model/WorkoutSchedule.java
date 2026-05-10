package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workout_schedules", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "day_of_week"})
})
public class WorkoutSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "gym_time")
    private String gymTime;

    @Column(name = "muscle_group")
    private String muscleGroup;

    private String notes;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ScheduleEntry> entries = new ArrayList<>();

    public WorkoutSchedule() {}

    public Long getId() { return id; }
    public User getUser() { return user; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public String getGymTime() { return gymTime; }
    public String getMuscleGroup() { return muscleGroup; }
    public String getNotes() { return notes; }
    public List<ScheduleEntry> getEntries() { return entries; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public void setGymTime(String gymTime) { this.gymTime = gymTime; }
    public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setEntries(List<ScheduleEntry> entries) { this.entries = entries; }
}
