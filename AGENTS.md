# AGENTS.md

## Purpose

Use this file as the catalog of Copilot agents and skills for RestorFX.

## Canonical Instructions

- See [.github/copilot-instructions.md](.github/copilot-instructions.md) for project rules and constraints.
- See [.github/instructions/](.github/instructions/) for detailed instruction files.

## Project Snapshot

- Java 8 JavaFX desktop app
- Layered monolith: controller -> service -> repository -> model
- Integration layer for external systems (NFe)

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
- ai-automation-engineer

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