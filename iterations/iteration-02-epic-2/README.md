# Iteration 02 - EPIC 2 (Database Layer)

## Goal

Establish the foundational persistence layer using SQLite and automated schema bootstrap.

## Delivered

- SQLite connection manager with automatic local database directory creation
- Startup database initializer that executes SQL schema from classpath resource
- Initial schema with tables for menu, customer, order header, and order item
- Base repository abstraction for upcoming data access implementations
- UI status text updated to indicate EPIC 2 database readiness

## Exit Criteria

- Application initializes SQLite schema on startup without manual setup
- Database file is created locally under db/restaurant.db
- Project is ready for EPIC 3 (Menu Management)
