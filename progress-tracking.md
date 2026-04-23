# Progress Tracking

## Last Iteration

Iteration: **4 (EPIC 4 - Customer Management MVP)**

What was done:

- Customer domain model was added with identity and contact fields.
- CustomerRepository was implemented with SQLite CRUD operations plus total counting.
- CustomerService was added with field normalization and validation rules.
- JavaFX screen now has a dedicated Customers tab with add/edit/delete actions.
- MainController is wired to both menu and customer services with independent feedback states.
- Service and repository tests for customer behavior were added.
- Schema now contains idempotent sample customer data for better first-run experience.

What is next:

- Start EPIC 6 with a mocked NFe integration service flow.
- Keep EPIC 5 intentionally skipped.
