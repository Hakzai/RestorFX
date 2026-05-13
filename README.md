# Restaurant Management

Current state:

- EPIC 5 is complete with the order / sales flow implemented.
- EPIC 6 is complete with mock NFe integration and fiscal audit persistence.
- EPIC 7 is in progress for real NFe provider integration.

## Recent highlights

- Order and OrderItem models added with repository and service support.
- JavaFX Orders tab added for recent orders and new order entry.
- Order creation validates customer, items, quantity, and menu item existence.
- Order persistence uses JDBC transactions and loads recent orders with item details.
- Mock NFe emission contract, adapter, and audit persistence are available.
- Real NFe provider setup and emission are under active development.

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
- [x] EPIC 5 - Order / Sales Flow (Basic)
- [x] EPIC 6 - NFe Integration (Mock)
- [x] EPIC 7 - Real NFe Integration
- [ ] EPIC 8 - Backup & Reliability
- [ ] EPIC 9 - Packaging & Delivery

## EPIC 7 notes

- The legacy mock tab is controlled by the `restaurant.feature.nfe.mock.enabled` system property.
- The active provider can be configured with `restaurant.nfe.provider` (`MOCK` or `REAL_PROVIDER`; legacy `REAL_STUB` is still accepted).
- EPIC 7 setup validation runs in the service layer using Hibernate Validator plus Java KeyStore checks.
- Leave the mock tab enabled during local mock testing; set it to `false` to use the real provider setup flow.
- The `NFe Real` tab is the starting point for provider and certificate configuration.
- Iteration details are in `iterations/iteration-07-epic-7/README.md`.
