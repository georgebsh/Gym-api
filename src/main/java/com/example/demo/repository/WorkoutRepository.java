package com.example.demo.repository;

import com.example.demo.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserUsername(String username);
}
