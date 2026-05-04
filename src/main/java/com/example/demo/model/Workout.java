package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workouts")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    private LocalDate date;
    private String notes;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<WorkoutEntry> entries = new ArrayList<>();

    public Workout() {}

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getName() { return name; }
    public LocalDate getDate() { return date; }
    public String getNotes() { return notes; }
    public List<WorkoutEntry> getEntries() { return entries; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setName(String name) { this.name = name; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setEntries(List<WorkoutEntry> entries) { this.entries = entries; }
}
