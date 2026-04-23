# Iteration 07 - EPIC 7 (Real NFe Integration Kickoff)

## Goal

Start EPIC 7 by preparing the UI and runtime toggle strategy for real provider integration, while preserving the legacy mock flow behind a feature flag.

## Delivered

- Introduced runtime feature flag `restaurant.feature.nfe.mock.enabled`
- Added provider property support via `restaurant.nfe.provider`
- Legacy JavaFX `NFe Mock` tab is now disable-able through the feature flag (without deletion)
- Added a dedicated JavaFX `NFe Real` tab for provider and certificate setup inputs
- Added provider selection (`MOCK` / `REAL_STUB`) in the EPIC 7 tab
- Added `NFeServiceFactory` + `NFeProvider` for runtime service resolution
- Added `RealNFeService` stub adapter to prepare real integration path
- Added controller handlers for EPIC 7 setup validation and setup note generation
- Updated project docs to record the EPIC 7 kickoff scope
- Added tests for feature flags, provider factory behavior, and real provider stub emission

## Exit Criteria

- Legacy mock flow remains available by default for backward compatibility
- Setting `restaurant.feature.nfe.mock.enabled=false` disables the mock tab
- Provider can be selected and applied from EPIC 7 tab without code changes
- A new, separate tab exists for EPIC 7 real-provider setup workflow
- Project compiles successfully after the changes

## Next Slice

- Replace `RealNFeService` stub with concrete provider adapter implementation
- Add secure certificate loading (avoid plain text entry/storage) and provider-specific validation
- Persist and surface failure-path audit entries for rejected/errored emissions
- Add automated tests for provider switching from JavaFX controller actions
