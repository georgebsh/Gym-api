package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String bodyPart;

    private String targetArea;
    private String description;

    public Exercise() {}

    public Exercise(Long id, String name, String bodyPart, String targetArea, String description) {
        this.id = id;
        this.name = name;
        this.bodyPart = bodyPart;
        this.targetArea = targetArea;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getBodyPart() { return bodyPart; }
    public String getTargetArea() { return targetArea; }
    public String getDescription() { return description; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setBodyPart(String bodyPart) { this.bodyPart = bodyPart; }
    public void setTargetArea(String targetArea) { this.targetArea = targetArea; }
    public void setDescription(String description) { this.description = description; }
}
