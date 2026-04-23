# Architecture Overview

## Iteration 2 Snapshot

System style:

- Desktop monolith

Primary technologies:

- Java 8
- JavaFX (UI)
- Maven (build)
- SQLite JDBC with startup schema bootstrap
- SLF4J simple logger

Current layered structure:

- `controller`: JavaFX event/UI handlers
- `service`: business rules (planned)
- `repository`: base repository abstraction available
- `model`: domain entities (planned)
- `dto`: data transfer objects (planned)
- `integration/nfe`: external fiscal integration adapters (planned)

Runtime flow in current iteration:

- `App` initializes database schema at startup
- `App` starts JavaFX
- FXML (`main.fxml`) loads UI and controller
- `MainController` initializes view state

Next architectural increment:

- Implement EPIC 3 menu module with model, repository CRUD, and service rules
