package com.example.demo.service;

import com.example.demo.model.Exercise;
import com.example.demo.repository.ExerciseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ExerciseService {

    private final ExerciseRepository repository;

    public ExerciseService(ExerciseRepository repository) {
        this.repository = repository;
    }

    public List<Exercise> getAll() {
        return repository.findAll();
    }

    public List<Exercise> getByBodyPart(String bodyPart) {
        return repository.findByBodyPartIgnoreCase(bodyPart);
    }

    public Exercise create(Exercise exercise) {
        return repository.save(exercise);
    }

    public Exercise update(Long id, Exercise updated) {
        Exercise existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found"));
        existing.setName(updated.getName());
        existing.setBodyPart(updated.getBodyPart());
        existing.setTargetArea(updated.getTargetArea());
        existing.setDescription(updated.getDescription());
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found");
        }
        repository.deleteById(id);
    }
}
