# Progress Tracking

## Last Iteration

Iteration: **3 (EPIC 3 - Menu Management MVP)**

What was done:

- MenuItem domain model was added to represent menu entities.
- MenuItemRepository was implemented with SQLite CRUD operations.
- MenuItemService was added with basic validation rules and normalization.
- JavaFX screen now supports menu listing plus add/edit/delete actions.
- MainController is wired to MenuItemService for CRUD operations and live status updates.
- Service and repository test suites were added and validated with `mvn clean test`.
- Schema now contains idempotent sample menu items for better first-run experience.

What is next:

- Start EPIC 4 with Customer model/repository/service baseline.
- Add initial customer CRUD JavaFX screen.
