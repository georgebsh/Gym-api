package com.example.demo.repository;

import com.example.demo.model.ScheduleEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleEntryRepository extends JpaRepository<ScheduleEntry, Long> {
}
