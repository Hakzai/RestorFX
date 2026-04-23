# Iteration 07 - EPIC 7 (Real NFe Integration Kickoff)

## Goal

Start EPIC 7 by preparing the UI and runtime toggle strategy for real provider integration, while preserving the legacy mock flow behind a feature flag.

## Delivered

- Introduced runtime feature flag `restaurant.feature.nfe.mock.enabled`
- Legacy JavaFX `NFe Mock` tab is now disable-able through the feature flag (without deletion)
- Added a dedicated JavaFX `NFe Real` tab for provider and certificate setup inputs
- Added controller handlers for EPIC 7 setup validation and setup note generation
- Updated project docs to record the EPIC 7 kickoff scope

## Exit Criteria

- Legacy mock flow remains available by default for backward compatibility
- Setting `restaurant.feature.nfe.mock.enabled=false` disables the mock tab
- A new, separate tab exists for EPIC 7 real-provider setup workflow
- Project compiles successfully after the changes

## Next Slice

- Implement a concrete real-provider adapter behind `NFeService`
- Add secure certificate loading and environment-specific provider config
- Persist and surface failure-path audit entries for rejected/errored emissions
- Add automated tests for EPIC 7 feature-flag behavior and provider selection flow
