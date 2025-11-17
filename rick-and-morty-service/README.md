# Recruitment Task - Rick and Morty Service

## Table of Contents

- [Features](#features)
- [Project Structure](#project-structure)
- [Quick Start](#quick-start)
- [API Documentation](#api-documentation)
- [Architecture](#architecture)
- [Configuration](#configuration)
- [Testing](#testing)

## Features

### Core Features
- **Asynchronous Data Import** - Background import from external API using CompletableFuture
- **Character Search** - Search characters by name with pagination
- **Message Queue** - RabbitMQ for reliable async processing
- **Database** - MariaDB with Flyway migrations
- **Caching** - Redis-based caching for improved performance
- **Validation** - Jakarta Bean Validation

## Project Structure

```
rick-and-morty-service/     # Main Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/rickandmorty/
│   │   │   │   ├── client/              # HTTP clients
│   │   │   │   ├── config/              # Configuration classes
│   │   │   │   ├── controller/          # REST controllers
│   │   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── entity/              # JPA entities
│   │   │   │   ├── exception/           # Custom exceptions
│   │   │   │   ├── mapper/              # Object mappers
│   │   │   │   ├── messaging/           # RabbitMQ producers/consumers
│   │   │   │   ├── repository/          # Spring Data repositories
│   │   │   │   └── service/             # Business logic
│   │   │   └── resources/
│   │   │       ├── application.yml      # Application configuration
│   │   │       └── db/migration/        # Flyway migrations
│   │   └── test/                        # Unit and integration tests
│   ├── .env.example                     # Environment variables template
│   └── pom.xml
└── README.md                    # This file
```

---

## Quick Start

### Prerequisites

- **Java 21+**
- **Maven 3.8+**
- **Docker & Docker Compose**

### 1. Clone the Repository

```bash
git clone https://github.com/klimovitch/recruitment-task.git
cd recruitment-task
```

### 2. Start Infrastructure

Start MariaDB, RabbitMQ, and Redis using Docker Compose:

```bash
docker-compose up -d
```

This will start:
- **MariaDB** on port `3306`
- **RabbitMQ** on ports `5672` (AMQP) and `15672` (Management UI)
- **Redis** on port `6379`

### 3. Configure Environment

Copy the example environment file:

```bash
cd rick-and-morty-service
cp .env.example .env
```

Edit `.env` with your credentials if needed (defaults work for local development).

### 4. Build the Application

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR:

```bash
java -jar target/rick-and-morty-service-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8089**

## API Documentation

### Swagger UI

Once the application is running, access interactive API documentation at:

**http://localhost:8089/swagger-ui.html**

### API Endpoints

#### Import Characters
```bash
# Trigger import (returns immediately, processes in background)
curl -u admin:admin123 -X POST http://localhost:8089/api/characters/import
```

Response:
```
HTTP/1.1 202 Accepted
```

#### Search Characters
```bash
# Search with pagination
curl -u user:user123 "http://localhost:8089/api/characters/search?name=Rick&page=0&size=20"
```

Response:
```json
{
  "content": [
    {
      "id": 1,
      "name": "Rick Sanchez",
      "status": "Alive",
      "species": "Human",
      "gender": "Male",
      "origin": "Earth (C-137)",
      "location": "Citadel of Ricks"
    }
  ],
  "totalElements": 42,
  "totalPages": 3,
  "size": 20,
  "number": 0
}
```
## Architecture


```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ HTTP
       ▼
┌─────────────────────────────────────────┐
│     Rick and Morty Service              │
│  ┌────────────┐  ┌──────────────────┐   │
│  │ REST API   │  │  Business Logic  │   │
│  └─────┬──────┘  └────────┬─────────┘   │
│        │                  │             │
│        ▼                  ▼             │
│  ┌─────────┐       ┌──────────┐         │
│  │  Redis  │       │ RabbitMQ │         │
│  └─────────┘       └────┬─────┘         │
│                         │               │
│                         ▼               │
│                   ┌──────────┐          │
│                   │ MariaDB  │          │
│                   └──────────┘          │
└─────────────────────────────────────────┘
              │
              ▼
    ┌──────────────────┐
    │ Rick & Morty API │
    └──────────────────┘
```

### Key Design Patterns

- **Layered Architecture** - Controllers, Services, Repositories
- **Producer-Consumer** - RabbitMQ for async processing
- **Cache-Aside** - Redis for query optimization
- **Repository Pattern** - Spring Data JPA abstraction
- **DTO Pattern** - Separation of API and domain models

---

## Configuration

### Environment Variables

All sensitive configuration is externalized via environment variables:

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_URL` | `jdbc:mariadb://mariadb:3306/rickmorty` | Database connection URL |
| `DB_USERNAME` | `dev` | Database username |
| `DB_PASSWORD` | `dev` | Database password |
| `RABBITMQ_HOST` | `rabbitmq` | RabbitMQ host |
| `RABBITMQ_PORT` | `5672` | RabbitMQ port |
| `RABBITMQ_USERNAME` | `guest` | RabbitMQ username |
| `RABBITMQ_PASSWORD` | `guest` | RabbitMQ password |
| `REDIS_HOST` | `redis` | Redis host |
| `REDIS_PORT` | `6379` | Redis port |
| `REDIS_PASSWORD` | `` | Redis password |
| `ADMIN_USERNAME` | `admin` | Admin username |
| `ADMIN_PASSWORD` | `admin123` | Admin password |
| `USER_USERNAME` | `user` | User username |
| `USER_PASSWORD` | `user123` | User password |
| `SERVER_PORT` | `8089` | Application port |

### Application Properties

Main configuration is in `rick-and-morty-service/src/main/resources/application.yml`:

## Testing

### Test Technologies

- **JUnit 5** - Testing framework
- **Mockito** - Mocking
- **Testcontainers** - Integration testing with real databases
- **Spring Boot Test** - Spring context testing
- **AssertJ** - Fluent assertions

---
