package com.example.demo.repository;

import com.example.demo.model.WorkoutSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface WorkoutScheduleRepository extends JpaRepository<WorkoutSchedule, Long> {
    List<WorkoutSchedule> findByUserUsername(String username);
    Optional<WorkoutSchedule> findByUserUsernameAndDayOfWeek(String username, DayOfWeek dayOfWeek);
}
