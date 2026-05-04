package com.example.demo.controller;

import com.example.demo.model.Exercise;
import com.example.demo.service.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    private final ExerciseService service;

    public ExerciseController(ExerciseService service) {
        this.service = service;
    }

    @GetMapping
    public List<Exercise> getAll() {
        return service.getAll();
    }

    @GetMapping("/{bodyPart}")
    public List<Exercise> getByBodyPart(@PathVariable String bodyPart) {
        return service.getByBodyPart(bodyPart);
    }

    @PostMapping
    public Exercise create(@RequestBody Exercise exercise) {
        return service.create(exercise);
    }

    @PutMapping("/{id}")
    public Exercise update(@PathVariable Long id, @RequestBody Exercise exercise) {
        return service.update(id, exercise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
