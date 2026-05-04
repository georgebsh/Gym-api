package com.example.demo.controller;

import com.example.demo.dto.WorkoutEntryRequest;
import com.example.demo.model.Workout;
import com.example.demo.model.WorkoutEntry;
import com.example.demo.service.WorkoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping
    public List<Workout> getAll(Principal principal) {
        return workoutService.getWorkoutsForUser(principal.getName());
    }

    @GetMapping("/{id}")
    public Workout get(@PathVariable Long id, Principal principal) {
        return workoutService.getWorkout(id, principal.getName());
    }

    @PostMapping
    public Workout create(@RequestBody Workout workout, Principal principal) {
        return workoutService.createWorkout(principal.getName(), workout);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        workoutService.deleteWorkout(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/entries")
    public WorkoutEntry addEntry(@PathVariable Long id,
                                 @RequestBody WorkoutEntryRequest request,
                                 Principal principal) {
        return workoutService.addEntry(id, request, principal.getName());
    }
}
