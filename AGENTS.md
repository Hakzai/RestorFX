# AGENTS.md

## Project Overview

RestorFX is a Java 8 desktop application built with JavaFX, Maven, SQLite, and JDBC. It follows a layered monolith architecture:

- controller: UI interaction only
- service: business rules only
- repository: persistence only
- model: entities and data structures
- integration: external adapters such as NFe

## Hard Rules

- Keep controllers free of business logic.
- Keep services free of UI access.
- Keep repositories limited to persistence.
- Use JDBC only for database access.
- Do not introduce Spring Boot, Hibernate/JPA, microservices, or unnecessary frameworks.
- Prefer the simplest working solution.
- Do not create unused abstractions or classes.

## Core Stack

- Java 8
- JavaFX
- Maven
- SQLite
- SLF4J for logging
- Jackson when JSON is needed
- Apache Commons only when it solves a real problem

## Current Direction

- EPIC 5 is complete for order/sales flow.
- EPIC 6 is complete for mock NFe integration.
- EPIC 7 is in progress for real NFe provider integration.

## Preferred Specialist Agents

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

## Preferred Skills

- crud-generator
- javafx-screen-builder
- sqlite-schema-sync
- maven-dependency-manager
- refactor-to-layered
- manual-test-checklist
- license-system-helper when working on trial/licensing
- create-implementation-plan for larger work
- create-architectural-decision-record for non-trivial decisions
- create-readme for documentation refreshes

## Working Style

- Read the local instructions first before coding.
- Keep changes small and focused.
- Prefer explicit, testable steps.
- Preserve existing behavior unless the task explicitly changes it.
- Validate after edits whenever possible.