# Architecture Overview

## Pattern
Layered Monolith

Layers:
- controller (UI interaction)
- service (business logic)
- repository (data access)
- model (entities)

## Rules
- Controllers must NOT contain business logic
- Services must NOT access UI
- Repositories must ONLY handle persistence
- No direct DB access outside repository layer

## Data Flow
UI → Controller → Service → Repository → Database

## Database
- SQLite
- Access via JDBC only
- No ORM (JPA/Hibernate not allowed)

## UI
- JavaFX (FXML)
- Keep UI logic minimal

## Integrations
- Must go through `integration` layer
- No direct external calls from services

## General Principles
- Keep it simple
- Avoid overengineering
- Build only what is necessary