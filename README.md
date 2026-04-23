# Restaurant Management - Iteration 2 (EPIC 2)

## Scope of this iteration

This iteration focuses on **EPIC 2 - Database Layer**.

Implemented in this iteration:

- SQLite connection manager implementation
- Database schema bootstrap on application startup
- Initial relational schema for menu, customer, order header, and order items
- Base repository abstraction for upcoming data access implementations
- JavaFX startup status updated to show EPIC 2 readiness

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

## Epic roadmap

- [x] EPIC 1 - Project Setup
- [x] EPIC 2 - Database Layer
- [ ] EPIC 3 - Menu Management
- [ ] EPIC 4 - Customer Management
- [ ] EPIC 5 - Skipped
- [ ] EPIC 6 - NFe Integration (Mock)
- [ ] EPIC 7 - Real NFe Integration
- [ ] EPIC 8 - Backup & Reliability
- [ ] EPIC 9 - Packaging & Delivery
