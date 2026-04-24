# Restaurant Management - Iteration 6.1 (EPIC 6)

## Scope of this iteration

This iteration focuses on **EPIC 6.1 - NFe Mock Audit Persistence**.

Implemented in this iteration:

- NFe emission contract with `NFeService` interface
- Mock integration adapter with `MockNFeService`
- Mock NFe XML generation with basic customer/amount payload
- XML logging for every mocked emission (for integration traceability)
- New JavaFX `NFe Mock` tab to emit and preview mocked XML
- Fiscal document audit persistence layer (`fiscal_document` table + repository/service)
- NFe tab now shows recent fiscal audit records with refresh support
- Unit tests covering mock emission output and input validation
- Unit and repository tests covering fiscal document persistence behavior

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
- [x] EPIC 6 - NFe Integration (Mock)
- [ ] EPIC 7 - Real NFe Integration
- [ ] EPIC 8 - Backup & Reliability
- [ ] EPIC 9 - Packaging & Delivery

## EPIC 7 starter notes

- The legacy mock tab is controlled by the `restaurant.feature.nfe.mock.enabled` system property.
- The active provider can be configured with `restaurant.nfe.provider` (`MOCK` or `REAL_PROVIDER`; legacy `REAL_STUB` is still accepted).
- EPIC 7.2 setup validation now runs in service layer using Hibernate Validator + Java KeyStore checks.
- Leave it enabled during local mock testing; set it to `false` to disable the mock tab and use the new EPIC 7 tab.
- The new `NFe Real` tab is the starting point for provider and certificate setup work.
- Iteration details for this kickoff are in `iterations/iteration-07-epic-7/README.md`.
