# Progress Tracking

## Last Iteration

Iteration: **6 (EPIC 6 - NFe Integration Mock MVP)**

What was done:

- NFe integration contract was introduced through `NFeService`.
- `MockNFeService` now simulates emission with validation and mock authorization data.
- Mocked XML output is generated and logged for each emission attempt.
- JavaFX gained an `NFe Mock` tab with emission form and XML preview area.
- Customer selection can prefill NFe customer fields to speed up manual testing.
- Unit tests were added for successful emission and validation failure scenarios.

What is next:

- Keep EPIC 5 intentionally skipped.
- Start EPIC 7 with a real NFe provider adapter behind the same `NFeService` contract.
