# TFG Backend

## Overview

This repository contains the Spring Boot backend API for the TFG project. It serves the Flutter frontend and provides REST endpoints for authentication, patient and caregiver workflows, medication management, appointments, routines, allergies, profiles, and relation linking.

## Key Features

- JWT-based authentication and authorization
- REST controllers for core features: users, auth, home, medication, appointments, routines, allergies, profile, and relations
- Appointment scheduling and patient/caregiver management
- Medication and prescription tracking
- Routine creation, editing, and status tracking
- Allergy registration and severity handling
- PostgreSQL persistence with Spring Data JPA

## Architecture

The backend code is organized using standard Spring Boot patterns:

- `src/main/java/com/tfg/proyecto/backend/controller/` - REST controllers grouped by feature
- `src/main/java/com/tfg/proyecto/backend/service/` - Business logic services
- `src/main/java/com/tfg/proyecto/backend/repository/` - JPA repositories
- `src/main/java/com/tfg/proyecto/backend/model/` - Domain and persistence entities
- `src/main/java/com/tfg/proyecto/backend/dto/` - Request and response DTOs
- `src/main/java/com/tfg/proyecto/backend/security/` - JWT filters and Spring Security configuration
- `src/main/java/com/tfg/proyecto/backend/config/` - Application beans and security bean configuration

## Technology Stack

- Java 17
- Spring Boot 3.4.10
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway (disabled by default)
- JWT via JJWT
- Lombok
- Hibernate Validator
- Apache Commons Lang

## Prerequisites

- Java 17 SDK
- Maven 3.x
- PostgreSQL database
- Environment variables configured for database and JWT settings

## Environment Configuration

The backend reads configuration values from environment variables via `application.properties`:

- `DB_URL` - PostgreSQL JDBC URL
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `JWT_KEY` - Secret key for JWT signing
- `JWT_EXPIRATION` - JWT expiration time in milliseconds

Example settings:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=none
spring.flyway.enabled=false
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

jwt.key=${JWT_KEY}
jwt.expiration=${JWT_EXPIRATION}
```

## Setup

1. Clone the repository:

```bash
git clone <repository-url>
cd backend
```

2. Configure PostgreSQL and export the required environment variables.

3. Build the application:

```bash
./mvnw clean install
```

## Run

Launch the backend application:

```bash
./mvnw spring-boot:run
```

Or run the packaged JAR:

```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## Testing

Run the backend test suite:

```bash
./mvnw test
```

## Notes

- The backend uses `spring.jpa.hibernate.ddl-auto=none` by default, so schema creation and migration must be handled externally or enabled according to your deployment strategy.
- Ensure the frontend `API_BASE_URL` points to the backend API base path, typically `http://localhost:8080/api`.
- The backend is designed for modular extension and clean separation between controllers, services, repositories, and DTOs.
