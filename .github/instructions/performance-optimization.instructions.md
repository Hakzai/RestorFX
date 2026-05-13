---
description: "Performance guidance for RestorFX focusing on JavaFX responsiveness, JDBC efficiency, and SQLite access patterns."
applyTo: "**/*.java, **/*.fxml, **/*.sql"
---

# Performance Optimization Guidance

## Goal

Keep the desktop app responsive and predictable without overengineering.

## Core Rules

- Keep expensive work off the JavaFX application thread.
- Use background tasks for disk, database, and network operations.
- Minimize round trips to SQLite.
- Load only the data the screen needs.
- Prefer simple caching only when repeated reads are proven to be a bottleneck.
- Avoid premature optimization.

## JavaFX Responsiveness

- Use `Task`, `Service`, or background executors for slow work.
- Update the UI only on the JavaFX thread.
- Avoid long loops or blocking calls in event handlers.
- Keep table refreshes and scene initialization lightweight.

## JDBC and SQLite

- Reuse connections through the existing application access pattern.
- Use prepared statements and narrow result sets.
- Fetch only needed columns.
- Add indexes only for queries that are actually used and measured.
- Prefer pagination or incremental loading for large lists.

## UI and Data Loading

- Load table data lazily when possible.
- Avoid reloading the same reference data repeatedly.
- Keep FXML layouts simple and avoid unnecessary nested containers.
- Recompute totals and summaries in the service layer, not in the controller.

## NFe and External Calls

- Treat external provider calls as slow operations.
- Run them asynchronously.
- Add retry logic only when the business case requires it.
- Bound timeouts so the UI remains responsive.

## Review Checklist

- No blocking work on the UI thread.
- Database access is scoped and efficient.
- Tables do not load more rows than needed.
- Background operations have clear completion and error handling.