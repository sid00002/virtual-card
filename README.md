# 💳 Virtual Card Platform

A production-grade **Virtual Card Management System** built using **Java + Spring Boot** following clean architecture and financial-system best practices.

The platform allows:

* Create virtual cards
* Top-up balance
* Spend from card with idempotency protection
* Retrieve transaction history (paginated)
* Automatic card expiration via scheduler
* Concurrency-safe transactions
* Observability with logging & metrics
* Integration testing with Testcontainers

This project is designed to demonstrate **real-world backend engineering skills** for fintech systems.

---

# 🚀 Features

## Card Management

* Create card
* Get card details
* List all cards
* Automatic expiration of cards

## Transactions

* Spend from card (idempotent)
* Top-up balance
* Retrieve transaction history with pagination

## Reliability & Safety

* Idempotency support for payment operations
* Row-level locking to prevent race conditions
* Optimistic concurrency handling
* Validation and domain exceptions

## Observability

* Structured logging
* Metrics instrumentation (Micrometer)
* Transaction latency monitoring

## Testing

* Unit tests
* Integration tests with Testcontainers
* Concurrency tests
* Scheduler tests

---

# 🏗 Architecture

The system follows **Clean Architecture / Hexagonal Principles**.

```
Controller Layer (API)
        ↓
Application Services
        ↓
Domain Model (Entities + Business Rules)
        ↓
Repository Interfaces
        ↓
Infrastructure (JPA, PostgreSQL, Flyway)
```

---

# 📐 Architecture Diagram

```
                ┌────────────────────┐
                │   Client / Postman  │
                └─────────┬──────────┘
                          │ HTTP
                          ▼
                ┌────────────────────┐
                │   REST Controllers  │
                └─────────┬──────────┘
                          │
                          ▼
                ┌────────────────────┐
                │ Application Layer  │
                │  - CardService     │
                │  - TransactionSvc  │
                │  - IdempotencySvc  │
                └─────────┬──────────┘
                          │
                          ▼
                ┌────────────────────┐
                │   Domain Layer     │
                │   Entities & Rules │
                └─────────┬──────────┘
                          │
                          ▼
                ┌────────────────────┐
                │ Infrastructure     │
                │ JPA / PostgreSQL   │
                │ Flyway Migrations  │
                └────────────────────┘
```

---

# 🧠 Key Engineering Concepts Used

## ✅ Idempotency (Financial-grade Safety)

Spend API supports **Idempotency-Key header** to prevent duplicate charges.

If client retries the same request:

* First request → processed normally
* Subsequent requests → return cached response

Ensures **exactly-once semantics**.

---

## ✅ Row-Level Locking

```
SELECT ... FOR UPDATE
```

Prevents concurrent balance corruption when multiple spend requests happen simultaneously.

---

## ✅ Transaction Management

Spring `@Transactional` ensures:

* Atomic updates
* Rollback on failure
* Consistency guarantees

---

## ✅ Scheduler

Automatic expiration of cards using:

```
@Scheduled
```

---

## ✅ Pagination

Transaction history uses Spring `Pageable` for scalable data retrieval.

---

## ✅ Observability

* SLF4J Logging
* Micrometer Metrics
* Latency timers
* Success counters

---

## ✅ Testcontainers

Integration tests run against **real PostgreSQL in Docker**, not H2.

This provides production-like reliability.

---

# 🛠 Tech Stack

| Technology      | Purpose               |
| --------------- | --------------------- |
| Java 17         | Language              |
| Spring Boot     | Framework             |
| Spring Data JPA | Persistence           |
| PostgreSQL      | Database              |
| Flyway          | DB migrations         |
| Docker          | Containerization      |
| Testcontainers  | Integration testing   |
| Micrometer      | Metrics               |
| JUnit 5         | Testing               |
| Mockito         | Unit testing          |
| Lombok          | Boilerplate reduction |
| Maven           | Build tool            |

---

# 📡 API Endpoints

## Create Card

```
POST /cards
```

Request:

```json
{
  "cardholderName": "John Doe",
  "initialBalance": 1000
}
```

---

## Get Card

```
GET /cards/{id}
```

---

## Get All Cards

```
GET /cards
```

---

## Top Up Card

```
POST /transactions/topup
```

Request:

```json
{
  "cardId": "uuid",
  "amount": 500
}
```

---

## Spend From Card (Idempotent)

```
POST /transactions/spend
```

Headers:

```
Idempotency-Key: unique-key-123
```

Request:

```json
{
  "cardId": "uuid",
  "amount": 100
}
```

---

## Transaction History (Paginated)

```
GET /transactions/{cardId}?page=0&size=10
```

---

# ⏰ Scheduled Jobs

Card expiration job runs periodically:

* Marks expired cards as `EXPIRED`
* Runs automatically in background

---

# 📊 Metrics

Available via Actuator:

```
/actuator/metrics
```

Examples:

* `transactions.success`
* `transactions.latency`

---

# 🧪 Running Tests

Run all tests:

```
mvn test
```

Includes:

* Unit tests
* Integration tests
* Concurrency tests
* Scheduler tests

---

# 🐳 Running Locally

Start database:

```
docker compose up -d
```

Run application:

```
mvn spring-boot:run
```

---

# 🗄 Database Migrations

Managed using Flyway.

Migration files:

```
src/main/resources/db/migration
```

---

# 📁 Project Structure

```
src/main/java
    ├── api
    ├── application
    ├── domain
    ├── infrastructure
    └── config

src/test/java
    ├── unit
    ├── integration
    ├── concurrency
    └── scheduler
```

---

# 🔐 Error Handling

Standardized API response format:

```json
{
  "success": false,
  "error": {
    "code": "INSUFFICIENT_FUNDS",
    "message": "Balance too low"
  }
}
```

---

# 📈 Production-Grade Practices Demonstrated

* Idempotent financial operations
* Concurrency control
* Clean architecture separation
* Database migrations
* Observability & metrics
* Integration testing with containers
* Scheduler automation
* Pagination support
* Domain-driven design principles

---

# 🚀 Future Improvements

* Redis-based distributed idempotency
* Kafka event publishing
* Card freeze/unfreeze API
* Fraud detection rules
* Rate limiting
* Multi-currency support
* Authentication & authorization
* Kubernetes deployment

---

# 👨‍💻 Author

Built as a backend engineering showcase project demonstrating fintech-grade architecture and reliability patterns.

---

# ⭐ If You Like This Project

Give it a star on GitHub!
