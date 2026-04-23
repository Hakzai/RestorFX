# Restaurant Management - Iteration 1 (EPIC 1)

## Scope of this iteration

This first iteration focuses on **EPIC 1 - Project Setup**.

Implemented in this iteration:

- Maven project created in this repository
- Java 8 compiler target configured
- Base package and resource structure created
- Dependencies added: SQLite, Lombok (provided), and SLF4J simple logger
- Initial JavaFX window implemented (`App` + `main.fxml`)

## Tech stack

- Java 8
- JavaFX
- Maven
- SQLite (planned in next epics)

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

## Epic roadmap

- [x] EPIC 1 - Project Setup
- [ ] EPIC 2 - Database Layer
- [ ] EPIC 3 - Menu Management
- [ ] EPIC 4 - Customer Management
- [ ] EPIC 5 - Skipped
- [ ] EPIC 6 - NFe Integration (Mock)
- [ ] EPIC 7 - Real NFe Integration
- [ ] EPIC 8 - Backup & Reliability
- [ ] EPIC 9 - Packaging & Delivery
