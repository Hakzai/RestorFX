# Progress Tracking

## Last Iteration

Iteration: **6.1 (EPIC 6 - NFe Mock Audit Persistence)**

What was done:

- NFe integration contract was introduced through `NFeService`.
- `MockNFeService` now simulates emission with validation and mock authorization data.
- Mocked XML output is generated and logged for each emission attempt.
- JavaFX gained an `NFe Mock` tab with emission form and XML preview area.
- Customer selection can prefill NFe customer fields to speed up manual testing.
- Fiscal document persistence was added with `fiscal_document` schema, model, repository, and service.
- Every successful NFe mock emission now records an auditable fiscal document row.
- NFe tab now includes a recent audit history panel for quick verification.
- Unit and repository tests now cover fiscal document persistence scenarios.

What is next:

- Keep EPIC 5 intentionally skipped.
- Start EPIC 7 with a real NFe provider adapter behind the same `NFeService` contract.

## Current Iteration (In Progress)

Iteration: **7 (EPIC 7 - Real NFe Integration Kickoff)**

What has started:

- Legacy mock UI flow is now controlled by `restaurant.feature.nfe.mock.enabled`.
- A dedicated `NFe Real` tab was added for provider and certificate setup.
- EPIC 7 kickoff details are tracked in `iterations/iteration-07-epic-7/README.md`.
- Provider selection (`MOCK` / `REAL_STUB`) now applies at runtime via `NFeServiceFactory`.
- A `RealNFeService` stub adapter is now wired for initial non-mock provider flow.

What is next:

- Replace `RealNFeService` stub with concrete provider adapter logic.
- Add secure certificate handling and provider-specific configuration validation.
