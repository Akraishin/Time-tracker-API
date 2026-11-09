# Time Tracker API

A lightweight Spring Boot REST API for managing projects, users, and tracking work hours.

## Features

- **Time Logging**: Log hours with business validations (max 24h/day per user, project deadline checks, user status).
- **Filtering & Pagination**: Dynamic query filtering using Spring Data JPA `Specification` and `Pageable`.
- **Clean Architecture**: Controller-Service-Repository pattern with DTO mapping and Bean Validation (`jakarta.validation`).
- **Error Handling**: Centralized exception handling via `@RestControllerAdvice`.
- **Testing**: Unit tests with JUnit 5 and Mockito.

## Tech Stack

- Java 17+
- Spring Boot 4 (Web MVC, Data JPA, Validation)
- H2 Database
- JUnit 5 & Mockito
- Maven

## API Endpoints

### Time Logs
- `GET /timelogs` — List time logs with pagination (`page`, `size`, `sort`) and filters (`id`, `startDate`, `endDate`)
- `POST /timelogs` — Create a new time log

### Projects
- `GET /projects` — List projects (supports pagination and filtering)
- `POST /projects` — Create a project
- `GET /projects/{id}` — Get project by ID
- `DELETE /projects/{id}` — Deactivate project

### Users
- `GET /users` — List users (supports pagination and filtering)
- `POST /users` — Create a user
- `GET /users/{id}` — Get user by ID
- `DELETE /users/{id}` — Deactivate user

## Quick Start

### Run tests
```bash
./mvnw clean test
```

### Run application
```bash
./mvnw spring-boot:run
```

The service will start at `http://localhost:8080`.  
H2 Console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./db/H2DB`, User: `sa`, Password: *empty*).
