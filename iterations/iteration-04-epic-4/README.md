# Iteration 04 - EPIC 4 (Customer Management MVP)

## Goal

Deliver an end-to-end first version of Customer Management, including backend flow, JavaFX CRUD UI, and automated tests.

## Delivered

- `Customer` domain model with core fields (`id`, `name`, `document`, `phone`, `email`)
- `CustomerRepository` CRUD operations using SQLite (`findAll`, `findById`, `create`, `update`, `deleteById`)
- `CustomerService` business layer with validation and normalization
- JavaFX customer management UI (table + form) in a dedicated `Customers` tab
- Main subtitle now reflects customer count and active menu items
- JUnit tests for customer service validation and repository CRUD integration
- Idempotent sample seed data for customers in schema bootstrap

## Exit Criteria

- Customer CRUD works from JavaFX UI using service/repository layers
- Service layer validates required customer name and basic email format
- Repository CRUD behavior is covered by integration tests
- `mvn clean test` passes with all EPIC 4 tests green

## Next slice

- Start EPIC 6 with mocked NFe integration service and initial UI/service wiring
- Keep EPIC 5 intentionally skipped
