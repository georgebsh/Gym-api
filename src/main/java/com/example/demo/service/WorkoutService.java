package com.example.demo.service;

import com.example.demo.dto.WorkoutEntryRequest;
import com.example.demo.model.Exercise;
import com.example.demo.model.User;
import com.example.demo.model.Workout;
import com.example.demo.model.WorkoutEntry;
import com.example.demo.repository.ExerciseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkoutEntryRepository;
import com.example.demo.repository.WorkoutRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutEntryRepository workoutEntryRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    public WorkoutService(WorkoutRepository workoutRepository,
                          WorkoutEntryRepository workoutEntryRepository,
                          ExerciseRepository exerciseRepository,
                          UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.workoutEntryRepository = workoutEntryRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
    }

    public List<Workout> getWorkoutsForUser(String username) {
        return workoutRepository.findByUserUsername(username);
    }

    public Workout getWorkout(Long id, String username) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found"));
        if (!workout.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        return workout;
    }

    public Workout createWorkout(String username, Workout workout) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        workout.setUser(user);
        return workoutRepository.save(workout);
    }

    public void deleteWorkout(Long id, String username) {
        workoutRepository.delete(getWorkout(id, username));
    }

    public WorkoutEntry addEntry(Long workoutId, WorkoutEntryRequest request, String username) {
        Workout workout = getWorkout(workoutId, username);
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found"));
        WorkoutEntry entry = new WorkoutEntry();
        entry.setWorkout(workout);
        entry.setExercise(exercise);
        entry.setSets(request.getSets());
        entry.setReps(request.getReps());
        entry.setWeight(request.getWeight());
        return workoutEntryRepository.save(entry);
    }
}
