# Virtual Card Issuance Platform

## Overview

This project implements a robust, scalable backend service for a virtual card platform. The system supports card issuance, top-ups, spending, transaction tracking, idempotent financial operations, concurrency safety, observability, and scheduled processing.

The design focuses on clean architecture, correctness under concurrent access, and production-ready engineering practices.

---

## Features Implemented

### Core Functionalities

* Create virtual card with initial balance
* Spend from card with balance validation
* Top-up card balance
* Retrieve card details
* Retrieve transaction history with pagination
* Idempotent financial operations
* Concurrency-safe balance updates
* Scheduled card expiration
* Observability with logging and metrics
* Automated testing with Testcontainers
* Dockerized deployment

---

## Technology Stack

* Java 17
* Spring Boot
* Spring Data JPA (Hibernate)
* PostgreSQL
* Flyway (Database migrations)
* Testcontainers (Integration testing)
* JUnit 5 & Mockito
* Micrometer + Actuator (Metrics)
* Docker & Docker Compose
* Maven

---

## Architecture

The project follows Clean Architecture principles with separation of concerns.

Layers:

* API Layer (Controllers, DTOs, Request/Response models)
* Application Layer (Business services)
* Domain Layer (Entities, enums, exceptions)
* Infrastructure Layer (Repositories, persistence)
* Configuration Layer (Security, metrics, scheduler, idempotency)

Key design goals:

* Maintainability
* Testability
* Extensibility
* Concurrency safety
* Production readiness

---

## Project Structure

```
src/main/java/com/virtualcard

api
  controller
  dto

application
  service

domain
  model
  repository
  exception
  enums

infrastructure
  persistence

config
scheduler
idempotency
```

Tests:

```
src/test/java/com/virtualcard

config
unit
integration
concurrency
performance
```

---

## Database

Relational database: PostgreSQL

Main tables:

* cards
* transactions
* idempotency_records

Flyway is used for schema migrations to ensure version-controlled database evolution.

---

## API Endpoints

### Create Card

POST /cards

Request:

```
{
  "cardholderName": "John Doe",
  "initialBalance": 1000
}
```

---

### Get Card Details

GET /cards/{cardId}

---

### Spend From Card

POST /transactions/spend

Headers:

Idempotency-Key: unique-key

Request:

```
{
  "cardId": "uuid",
  "amount": 100
}
```

---

### Top Up Card

POST /transactions/topup

Request:

```
{
  "cardId": "uuid",
  "amount": 200
}
```

---

### Transaction History (Paginated)

GET /transactions/{cardId}?page=0&size=10

---

## Idempotency

Financial operations are idempotent to prevent duplicate processing.

Mechanism:

* Client sends Idempotency-Key header
* Server stores request + response
* Duplicate requests return stored response

This prevents double charging due to retries or network failures.

---

## Concurrency Handling

To ensure balance correctness under concurrent usage:

* Pessimistic locking (SELECT FOR UPDATE)
* Transactional boundaries
* Atomic balance updates

This prevents race conditions and negative balances.

---

## Scheduled Jobs

A scheduler runs periodically to expire cards.

Behavior:

* Finds expired cards
* Updates status to CLOSED
* Runs automatically using Spring Scheduling

---

## Observability

Logging:

* Structured logs at service layer
* Request lifecycle logging
* Error logging

Metrics:

* Transaction success counter
* Transaction latency timer
* Exposed via Spring Boot Actuator

Endpoints:

/actuator/metrics
/actuator/health

---

## Testing Strategy

### Unit Tests

* Domain logic validation
* Service layer behavior
* Exception handling

### Integration Tests

* Repository layer with Testcontainers PostgreSQL
* API layer using MockMvc
* Idempotency flow validation

### Concurrency Tests

* Multiple threads spending simultaneously
* Balance consistency validation

### Performance Simulation

* Load simulation tests to observe behavior under stress

---

## Containerization (Docker)

The application is fully dockerized.

### Build Image

```
docker build -t virtual-card-platform .
```

### Run Container

```
docker run -p 8080:8080 virtual-card-platform
```

---

## Docker Compose (App + Database)

```
docker-compose up --build
```

This starts:

* Application container
* PostgreSQL container

---

## Running Locally (Without Docker)

Prerequisites:

* Java 17
* Maven
* PostgreSQL

Run:

```
mvn clean install
mvn spring-boot:run
```

---

## Key Design Decisions

* Clean architecture for long-term maintainability
* Idempotency to guarantee financial correctness
* Pessimistic locking for concurrency safety
* Pagination for scalable transaction retrieval
* Flyway for database versioning
* Testcontainers for realistic integration tests
* Metrics and logging for observability
* Docker for deployment consistency

---

## Tradeoffs and Time-Constrained Choices

* Monolithic architecture instead of microservices
* Simple scheduler instead of distributed job system
* Database locking instead of distributed locking
* Basic metrics instead of full observability stack

---

## How This System Can Scale

* Horizontal scaling with stateless service instances
* Read replicas for heavy query workloads
* Distributed caching (Redis)
* Message queues for async workflows
* Sharded databases for very large scale
* API Gateway with rate limiting
* Kubernetes deployment
* Event-driven architecture for transaction processing

---

## Future Improvements

* Kafka for asynchronous transaction events
* Redis for caching and distributed idempotency
* Distributed locks (Redis / ZooKeeper)
* Card network integration
* Fraud detection module
* Rate limiting and DDoS protection
* Multi-region deployment
* Full observability stack (Prometheus + Grafana)

---

## What I Would Do With More Time

* Introduce CQRS architecture
* Add event sourcing for financial audit trail
* Implement distributed idempotency store
* Improve performance benchmarking
* Add security layer (authentication/authorization)
* Implement circuit breakers and resilience patterns

---

## Key Learnings

During this project, the following concepts and technologies were explored and implemented:

* Clean Architecture design
* Idempotent API design for financial systems
* Concurrency control using database locking
* Scheduled background jobs in Spring Boot
* Metrics and monitoring using Micrometer and Actuator
* Custom exception handling with global handlers
* Automated testing strategy (unit, integration, concurrency)
* Testcontainers for real database testing
* Docker containerization
* Flyway database migrations
* Pagination with Spring Data
* Observability best practices
* Transaction management in Spring

---

## Submission Notes

The project demonstrates:

* Correctness of financial logic
* Robustness under concurrent access
* Production-ready engineering practices
* Maintainable and extensible architecture
* Comprehensive automated testing

---

## Author

Siddhesh Dongare – Virtual Card Platform
