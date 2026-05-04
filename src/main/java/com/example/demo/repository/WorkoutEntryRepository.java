package com.example.demo.repository;

import com.example.demo.model.WorkoutEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutEntryRepository extends JpaRepository<WorkoutEntry, Long> {
}
