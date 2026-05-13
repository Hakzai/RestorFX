# RestorFX Copilot Instructions

## Purpose

Use this repository as a Java 8 desktop application context, not a general-purpose framework project.

## Architecture

- Layered monolith
- controller -> service -> repository -> model
- integration layer for external systems such as NFe
- JavaFX UI with FXML
- SQLite via JDBC only

## Constraints

- Do not use Spring Boot.
- Do not use Hibernate/JPA.
- Do not access the database outside repositories.
- Do not put business logic in controllers.
- Do not put UI logic in services.
- Do not add abstractions unless they solve a concrete problem.

## Code Style

- Keep classes small and single-purpose.
- Prefer composition over inheritance.
- Use SLF4J for logging.
- Use prepared statements for SQL.
- Keep error messages user-friendly in the UI.

## Useful Workflow Agents

- architecture-reviewer
- security-reviewer
- debug-mode
- project-architecture-planner
- project-documenter
- code-reviewer
- auto-fix
- full-cycle
- regression-reviewer
- schema-migration
- feature-evolution

## Useful Skills

- crud-generator
- javafx-screen-builder
- sqlite-schema-sync
- maven-dependency-manager
- refactor-to-layered
- manual-test-checklist
- create-implementation-plan
- create-architectural-decision-record
- create-readme

## Current Roadmap

- EPIC 7: real NFe provider integration
- EPIC 8: backup and reliability
- EPIC 9: packaging and delivery
- EPIC 10+: i18n, UI/UX standardization, modular screens, config, auth, reporting, logging, versioning