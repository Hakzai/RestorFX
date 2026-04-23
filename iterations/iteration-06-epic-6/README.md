# Iteration 06 - EPIC 6 (NFe Integration Mock MVP)

## Goal

Deliver a first end-to-end mocked NFe emission flow, preserving clean architecture boundaries and enabling a future real provider integration in EPIC 7.

## Delivered

- `NFeService` integration contract for fiscal document emission
- `MockNFeService` implementation with request validation
- Mock access key/protocol generation and mocked XML payload composition
- XML output logging for observability during development
- JavaFX `NFe Mock` tab with form fields and XML output preview
- Controller integration for emitting and clearing mocked NFe data
- Unit tests covering happy path and input validation errors

### EPIC 6.1 Additions (Audit Persistence)

- New `fiscal_document` SQLite table for fiscal audit history
- `FiscalDocument` model with repository/service support
- NFe mock emission now persists an auditable fiscal document record
- JavaFX NFe tab now shows recent fiscal records with manual refresh
- Added repository and service tests for fiscal persistence flow

## Exit Criteria

- NFe mock can be emitted from JavaFX and recorded in audit persistence
- Service validates required customer name and positive total amount
- Mock output includes access key, protocol, and XML payload
- Recent fiscal audit records are visible from the NFe screen
- `mvn clean test` passes with EPIC 6 and EPIC 6.1 tests included

## Next Slice

- Keep EPIC 5 intentionally skipped
- Start EPIC 7 with real provider integration behind `NFeService`
- Add failure-path audit entries for rejected/errored NFe attempts
