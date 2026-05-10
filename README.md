# Gym API

A RESTful backend for managing personal gym workouts and weekly training schedules. Built with Spring Boot, secured with JWT authentication, and containerized with Docker.

## Tech Stack

- Java 17 + Spring Boot
- Spring Security + JWT
- Spring Data JPA + Hibernate
- Docker / Docker Compose
- Deployed on Railway

## Live API Docs

Once running, visit **`http://localhost:8080/swagger-ui.html`** to explore and test all endpoints interactively in your browser. Register, grab the JWT token, click **Authorize**, and try any endpoint live.

## Getting Started

### Run with Docker

```bash
docker-compose up --build
```

The API will be available at `http://localhost:8080`.

### Run locally

```bash
./gradlew bootRun
```

---

## Authentication

All endpoints except `/api/auth/**` require a Bearer token in the `Authorization` header.

```
Authorization: Bearer <your_token>
```

### Register

```
POST /api/auth/register
```
```json
{
  "username": "george",
  "email": "george@example.com",
  "password": "secret"
}
```

### Login

```
POST /api/auth/login
```
```json
{
  "username": "george",
  "password": "secret"
}
```

Both return:
```json
{ "token": "eyJ..." }
```

---

## Exercises

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/exercises` | List all exercises |
| GET | `/exercises/{bodyPart}` | Filter by body part (e.g. `chest`) |
| POST | `/exercises` | Create an exercise |
| PUT | `/exercises/{id}` | Update an exercise |
| DELETE | `/exercises/{id}` | Delete an exercise |

### Exercise object

```json
{
  "name": "Bench Press",
  "bodyPart": "chest",
  "targetArea": "upper chest",
  "description": "Flat barbell bench press"
}
```

---

## Workout Log

Track individual workout sessions with exercises, sets, reps, and weight.

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/workouts` | Get all your workouts |
| GET | `/api/workouts/{id}` | Get a specific workout |
| POST | `/api/workouts` | Create a workout |
| DELETE | `/api/workouts/{id}` | Delete a workout |
| POST | `/api/workouts/{id}/entries` | Add an exercise to a workout |

### Create a workout

```json
{
  "name": "Monday Push",
  "date": "2026-05-10",
  "notes": "Heavy day"
}
```

### Add an exercise entry

```
POST /api/workouts/{id}/entries
```
```json
{
  "exerciseId": 1,
  "sets": 4,
  "reps": 10,
  "weight": 80.0
}
```

---

## Weekly Schedule

Plan your gym week by day. Set your gym time, muscle group focus, and a list of exercises with sets and reps for each day. Query any day to get your full plan back.

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/schedule` | Get your full weekly plan |
| GET | `/api/schedule/suggestions` | Get exercises for unscheduled muscle groups |
| GET | `/api/schedule/{day}` | Get plan for a specific day |
| POST | `/api/schedule` | Create or update a day's plan |
| POST | `/api/schedule/{day}/exercises` | Add an exercise to a day |
| DELETE | `/api/schedule/exercises/{entryId}` | Remove one exercise from a day |
| DELETE | `/api/schedule/{day}` | Clear an entire day's plan |

`{day}` must be one of: `MONDAY`, `TUESDAY`, `WEDNESDAY`, `THURSDAY`, `FRIDAY`, `SATURDAY`, `SUNDAY`

### Get exercise suggestions

Returns exercises grouped by muscle groups you haven't scheduled yet this week — so if you've planned Chest and Back, it shows everything else (Legs, Shoulders, Arms, etc.) with exercises for each.

```
GET /api/schedule/suggestions
```
```json
[
  {
    "muscleGroup": "Legs",
    "exercises": [
      { "id": 5, "name": "Squat", "bodyPart": "Legs", "targetArea": "Quads", "description": "..." },
      { "id": 6, "name": "Romanian Deadlift", "bodyPart": "Legs", "targetArea": "Hamstrings", "description": "..." }
    ]
  },
  {
    "muscleGroup": "Shoulders",
    "exercises": [...]
  }
]
```

### Set up a day

```
POST /api/schedule
```
```json
{
  "dayOfWeek": "MONDAY",
  "gymTime": "6:00 PM",
  "muscleGroup": "Chest",
  "notes": "Push day"
}
```

### Add an exercise to a day

```
POST /api/schedule/MONDAY/exercises
```
```json
{
  "exerciseId": 1,
  "sets": 4,
  "reps": 10
}
```

### Query a day

```
GET /api/schedule/MONDAY
```
```json
{
  "dayOfWeek": "MONDAY",
  "gymTime": "6:00 PM",
  "muscleGroup": "Chest",
  "notes": "Push day",
  "entries": [
    {
      "id": 1,
      "exercise": {
        "name": "Bench Press",
        "bodyPart": "chest",
        "targetArea": "upper chest"
      },
      "sets": 4,
      "reps": 10
    }
  ]
}
```
