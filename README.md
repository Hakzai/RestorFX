# Restaurant Management - Iteration 4 (EPIC 4)

## Scope of this iteration

This iteration focuses on **EPIC 4 - Customer Management MVP**.

Implemented in this iteration:

- Customer domain model implementation
- Customer repository with SQLite CRUD methods
- Customer service layer with validation and normalization
- JavaFX customer management screen with list/add/edit/delete actions
- Service and repository tests for EPIC 4 behavior
- Idempotent sample seed data for customers

## Tech stack

- Java 8
- JavaFX
- Maven
- SQLite

## Prerequisites

- JDK 8 is required for the simplest setup (JavaFX is bundled in Java 8).
- If using JDK 11+, configure JavaFX SDK/OpenJFX modules separately.

## How to run (Java 8 environment)

1. Compile:

```bash
mvn clean compile
```

2. Run app (example):

```bash
java -cp target/classes com.akeir.restaurant.App
```

3. Verify database bootstrap:

- The application creates a local SQLite database file in `db/restaurant.db` when it starts.
- Schema initialization runs automatically using `src/main/resources/db/schema.sql`.

4. Run tests:

```bash
mvn clean test
```

## Epic roadmap

- [x] EPIC 1 - Project Setup
- [x] EPIC 2 - Database Layer
- [x] EPIC 3 - Menu Management
- [x] EPIC 4 - Customer Management
- [ ] EPIC 5 - Skipped
- [ ] EPIC 6 - NFe Integration (Mock)
- [ ] EPIC 7 - Real NFe Integration
- [ ] EPIC 8 - Backup & Reliability
- [ ] EPIC 9 - Packaging & Delivery
