# Iteration 03 - EPIC 3 (Menu Management MVP)

## Goal

Deliver an end-to-end first version of Menu Management, including backend flow, JavaFX CRUD UI, and automated tests.

## Delivered

- `MenuItem` domain model with core fields (`id`, `name`, `description`, `priceCents`, `active`)
- `MenuItemRepository` CRUD operations using SQLite (`findAll`, `findById`, `create`, `update`, `deleteById`)
- `MenuItemService` business layer with basic validation and normalized input handling
- JavaFX menu management UI with table + form and add/update/delete actions
- Startup subtitle now reflects active vs total menu items
- JUnit tests for service validation and repository CRUD integration
- Isolated test database support via configurable database directory/file system properties
- Idempotent seed records for sample menu items in schema bootstrap

## Exit Criteria

- Menu item CRUD works from JavaFX UI using service/repository layers
- Service layer validates required fields and non-negative price
- Repository CRUD behavior is covered by integration tests
- `mvn clean test` passes with all EPIC 3 tests green

## Next slice

- Start EPIC 4 customer module (model, repository, service)
- Build initial customer CRUD UI in JavaFX
- Add customer service and repository tests
