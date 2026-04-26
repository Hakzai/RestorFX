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

- EPIC 5 order / sales flow has now started and is being implemented as a late-start slice.
- Start EPIC 7 with a real NFe provider adapter behind the same `NFeService` contract.

## Current Iteration (In Progress)

Iteration: **7 (EPIC 7 - Real NFe Integration Kickoff)**

What has started:

- Legacy mock UI flow is now controlled by `restaurant.feature.nfe.mock.enabled`.
- A dedicated `NFe Real` tab was added for provider and certificate setup.
- EPIC 7 kickoff details are tracked in `iterations/iteration-07-epic-7/README.md`.
- Provider selection (`MOCK` / `REAL_PROVIDER`) now applies at runtime via `NFeServiceFactory`.
- `RealNFeService` now performs concrete HTTP emission using Apache HttpClient with retry strategy.
- EPIC 7.2 moved setup validation from controller to service (`NFeProviderConfigurationService`).
- Hibernate Validator was added to reduce custom validation code and keep layers clean.

What is next:

- Improve secure certificate handling lifecycle and add provider-specific configuration validation rules.
- Add explicit UI controls for retry count and timeout configuration.
