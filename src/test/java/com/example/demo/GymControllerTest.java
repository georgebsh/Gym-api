package com.example.demo;

import com.example.demo.model.Exercise;
import com.example.demo.repository.ExerciseRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GymControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllExercises_returnsSeededData() throws Exception {
        mockMvc.perform(get("/exercises"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @Test
    void getByBodyPart_returnsOnlyMatchingExercises() throws Exception {
        mockMvc.perform(get("/exercises/chest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bodyPart").value("Chest"));
    }

    @Test
    void getByBodyPart_isCaseInsensitive() throws Exception {
        mockMvc.perform(get("/exercises/BICEPS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @Test
    void createExercise_persistsAndReturnsWithId() throws Exception {
        Exercise newExercise = new Exercise(null, "Test Curl", "Biceps", "Overall", "A test exercise.");
        mockMvc.perform(post("/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newExercise)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Curl"));
    }

    @Test
    void deleteExercise_returns404WhenNotFound() throws Exception {
        mockMvc.perform(delete("/exercises/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void register_returnsJwtToken() throws Exception {
        String json = """
                {"username":"newuser1","email":"new1@test.com","password":"password123"}
                """;
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void register_duplicateUsername_returns409() throws Exception {
        String json = """
                {"username":"dupuser","email":"dup@test.com","password":"password123"}
                """;
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void login_withValidCredentials_returnsToken() throws Exception {
        String register = """
                {"username":"loginuser1","email":"login1@test.com","password":"password123"}
                """;
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(register));

        String login = """
                {"username":"loginuser1","password":"password123"}
                """;
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    private String registerAndGetToken(String username) throws Exception {
        String json = String.format(
                "{\"username\":\"%s\",\"email\":\"%s@test.com\",\"password\":\"password123\"}",
                username, username);
        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("token").asText();
    }

    @Test
    void createWorkout_withValidToken_returnsWorkout() throws Exception {
        String token = registerAndGetToken("workoutuser1");
        String workout = """
                {"name":"Chest Day","date":"2024-01-15","notes":"Great session"}
                """;
        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(workout))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Chest Day"))
                .andExpect(jsonPath("$.entries").isArray());
    }

    @Test
    void getWorkouts_withoutToken_returns403() throws Exception {
        mockMvc.perform(get("/api/workouts"))
                .andExpect(status().isForbidden());
    }

    @Test
    void addEntryToWorkout_returnsEntryWithExerciseDetails() throws Exception {
        String token = registerAndGetToken("entryuser1");

        String workoutJson = """
                {"name":"Leg Day","date":"2024-01-16","notes":""}
                """;
        MvcResult workoutResult = mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(workoutJson))
                .andReturn();
        Long workoutId = objectMapper.readTree(workoutResult.getResponse().getContentAsString())
                .get("id").asLong();

        Long exerciseId = exerciseRepository.findAll().get(0).getId();

        String entryJson = String.format(
                "{\"exerciseId\":%d,\"sets\":4,\"reps\":8,\"weight\":80.0}", exerciseId);
        mockMvc.perform(post("/api/workouts/" + workoutId + "/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(entryJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sets").value(4))
                .andExpect(jsonPath("$.reps").value(8))
                .andExpect(jsonPath("$.exercise.name").exists());
    }
}
