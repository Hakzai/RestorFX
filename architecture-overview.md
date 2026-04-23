# Architecture Overview

## Iteration 1 Snapshot

System style:

- Desktop monolith

Primary technologies:

- Java 8
- JavaFX (UI)
- Maven (build)
- SQLite JDBC dependency included for upcoming persistence layer
- SLF4J simple logger

Current layered structure:

- `controller`: JavaFX event/UI handlers
- `service`: business rules (planned)
- `repository`: data access (planned)
- `model`: domain entities (planned)
- `dto`: data transfer objects (planned)
- `integration/nfe`: external fiscal integration adapters (planned)

Runtime flow in current iteration:

- `App` starts JavaFX
- FXML (`main.fxml`) loads UI and controller
- `MainController` initializes view state

Next architectural increment:

- Add database configuration and repository base for SQLite in EPIC 2
