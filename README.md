# Recruitment Task – Multi-Module Solution

This repository contains solutions for two independent recruitment tasks:

1. **Rick and Morty Service (Spring Boot, REST API, Redis cache, RabbitMQ)**
2. **Pharmacy Sales Database (SQL schema + queries)**

Each task is isolated inside its own folder with a dedicated README and instructions.

---

## Repository Structure

```
recruitment-task/
│
├── pharmacy-sales-db/
│   ├── src/
│   └── README.md
│
└── rick-and-morty-service/
    ├── src/
    ├── pom.xml
    ├── Dockerfile
    ├── docker-compose.yml
    ├── .env.example
    └── README.md
```

---

## Rick and Morty Service

A Spring Boot REST API that integrates with the public **Rick & Morty API**,  
caches search results in Redis, and publishes import tasks into RabbitMQ.

Technologies used:
- Java 21
- Spring Boot 3
- Redis (cache)
- RabbitMQ (message queue)
- Docker / Docker Compose
- JUnit & Mockito

See detailed documentation here:  
👉 [`rick-and-morty-service/README.md`](rick-and-morty-service/README.md)

---

## Pharmacy Sales Database

A normalized relational database design (3NF) for a simplified pharmacy sales domain  
including products, agents, documents, and items.

Includes:
- `schema.sql` — full database schema
- `queries.sql` — required SQL queries
- `test-data.sql` — optional sample dataset
- ER diagram

See detailed documentation here:  
👉 [`pharmacy-sales-db/README.md`](pharmacy-sales-db/README.md)

---

## Running the tasks

Each module contains its own instructions:
- For API service → see the module’s README.
- For SQL → run the `.sql` scripts in SQL engine.

---

## Notes

- Sensitive configuration is externalized via environment variables.
- A template `.env.example` is provided inside `rick-and-morty-service/`.
- All modules are independent and can be reviewed separately.
