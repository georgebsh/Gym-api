package com.example.demo.controller;

import com.example.demo.dto.ScheduleEntryRequest;
import com.example.demo.dto.ScheduleRequest;
import com.example.demo.model.WorkoutSchedule;
import com.example.demo.service.WorkoutScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class WorkoutScheduleController {

    private final WorkoutScheduleService scheduleService;

    public WorkoutScheduleController(WorkoutScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public List<WorkoutSchedule> getFullSchedule(Principal principal) {
        return scheduleService.getFullSchedule(principal.getName());
    }

    @GetMapping("/{day}")
    public WorkoutSchedule getDay(@PathVariable String day, Principal principal) {
        return scheduleService.getScheduleForDay(principal.getName(), day);
    }

    @PostMapping
    public ResponseEntity<WorkoutSchedule> saveSchedule(@RequestBody ScheduleRequest request, Principal principal) {
        WorkoutSchedule saved = scheduleService.saveSchedule(principal.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/{day}/exercises")
    public WorkoutSchedule addExercise(@PathVariable String day,
                                       @RequestBody ScheduleEntryRequest request,
                                       Principal principal) {
        return scheduleService.addExercise(principal.getName(), day, request);
    }

    @DeleteMapping("/exercises/{entryId}")
    public ResponseEntity<Void> removeExercise(@PathVariable Long entryId, Principal principal) {
        scheduleService.removeExercise(principal.getName(), entryId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{day}")
    public ResponseEntity<Void> deleteDay(@PathVariable String day, Principal principal) {
        scheduleService.deleteScheduleForDay(principal.getName(), day);
        return ResponseEntity.noContent().build();
    }
}
