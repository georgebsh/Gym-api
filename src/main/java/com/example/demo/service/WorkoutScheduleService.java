package com.example.demo.service;

import com.example.demo.dto.MuscleGroupSuggestion;
import com.example.demo.dto.ScheduleEntryRequest;
import com.example.demo.dto.ScheduleRequest;
import com.example.demo.model.Exercise;
import com.example.demo.model.ScheduleEntry;
import com.example.demo.model.User;
import com.example.demo.model.WorkoutSchedule;
import com.example.demo.repository.ExerciseRepository;
import com.example.demo.repository.ScheduleEntryRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkoutScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WorkoutScheduleService {

    private final WorkoutScheduleRepository scheduleRepo;
    private final ScheduleEntryRepository entryRepo;
    private final ExerciseRepository exerciseRepo;
    private final UserRepository userRepo;

    public WorkoutScheduleService(WorkoutScheduleRepository scheduleRepo,
                                  ScheduleEntryRepository entryRepo,
                                  ExerciseRepository exerciseRepo,
                                  UserRepository userRepo) {
        this.scheduleRepo = scheduleRepo;
        this.entryRepo = entryRepo;
        this.exerciseRepo = exerciseRepo;
        this.userRepo = userRepo;
    }

    public List<WorkoutSchedule> getFullSchedule(String username) {
        return scheduleRepo.findByUserUsername(username);
    }

    public WorkoutSchedule getScheduleForDay(String username, String day) {
        DayOfWeek dayOfWeek = parseDayOfWeek(day);
        return scheduleRepo.findByUserUsernameAndDayOfWeek(username, dayOfWeek)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No schedule found for " + day));
    }

    public WorkoutSchedule saveSchedule(String username, ScheduleRequest request) {
        DayOfWeek dayOfWeek = parseDayOfWeek(request.getDayOfWeek());
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        WorkoutSchedule schedule = scheduleRepo
                .findByUserUsernameAndDayOfWeek(username, dayOfWeek)
                .orElse(new WorkoutSchedule());

        schedule.setUser(user);
        schedule.setDayOfWeek(dayOfWeek);
        schedule.setGymTime(request.getGymTime());
        schedule.setMuscleGroup(request.getMuscleGroup());
        schedule.setNotes(request.getNotes());

        return scheduleRepo.save(schedule);
    }

    public WorkoutSchedule addExercise(String username, String day, ScheduleEntryRequest request) {
        DayOfWeek dayOfWeek = parseDayOfWeek(day);
        WorkoutSchedule schedule = scheduleRepo.findByUserUsernameAndDayOfWeek(username, dayOfWeek)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No schedule found for " + day + ". Create one first."));

        if (!schedule.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        Exercise exercise = exerciseRepo.findById(request.getExerciseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found"));

        ScheduleEntry entry = new ScheduleEntry();
        entry.setSchedule(schedule);
        entry.setExercise(exercise);
        entry.setSets(request.getSets());
        entry.setReps(request.getReps());

        entryRepo.save(entry);
        return scheduleRepo.findById(schedule.getId()).orElseThrow();
    }

    public void removeExercise(String username, Long entryId) {
        ScheduleEntry entry = entryRepo.findById(entryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not found"));

        if (!entry.getSchedule().getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        entryRepo.delete(entry);
    }

    public void deleteScheduleForDay(String username, String day) {
        DayOfWeek dayOfWeek = parseDayOfWeek(day);
        WorkoutSchedule schedule = scheduleRepo.findByUserUsernameAndDayOfWeek(username, dayOfWeek)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No schedule found for " + day));

        if (!schedule.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        scheduleRepo.delete(schedule);
    }

    public List<MuscleGroupSuggestion> getSuggestions(String username) {
        Set<String> scheduledGroups = scheduleRepo.findByUserUsername(username).stream()
                .map(s -> s.getMuscleGroup() != null ? s.getMuscleGroup().toLowerCase() : "")
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        Map<String, List<Exercise>> byBodyPart = exerciseRepo.findAll().stream()
                .collect(Collectors.groupingBy(e -> e.getBodyPart().toLowerCase()));

        return byBodyPart.entrySet().stream()
                .filter(entry -> !scheduledGroups.contains(entry.getKey()))
                .map(entry -> new MuscleGroupSuggestion(
                        entry.getValue().get(0).getBodyPart(),
                        entry.getValue()))
                .collect(Collectors.toList());
    }

    private DayOfWeek parseDayOfWeek(String day) {
        try {
            return DayOfWeek.valueOf(day.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid day: '" + day + "'. Use MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, or SUNDAY.");
        }
    }
}
