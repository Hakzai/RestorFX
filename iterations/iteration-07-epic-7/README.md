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

### EPIC 7.2 Additions (Validation + Layering Alignment)

- Moved provider setup validation out of controller into `NFeProviderConfigurationService`
- Added `NFeProviderSetupRequest` DTO for setup input transport
- Added Hibernate Validator-based field validation for provider, environment, certificate path, and password
- Added PKCS12 certificate validation using Java standard `KeyStore`
- Added service-level tests for positive and negative setup validation flows

### Library Decision (per engineering guidelines)

- Evaluated options for validation: Apache Commons Validator, Hibernate Validator, manual checks
- Chosen: Hibernate Validator (`org.hibernate.validator:hibernate-validator`)
- Why: stable ecosystem, standard bean validation API, reduces custom validation code, keeps controller thin

## Exit Criteria

- Legacy mock flow remains available by default for backward compatibility
- Setting `restaurant.feature.nfe.mock.enabled=false` disables the mock tab
- Provider can be selected and applied from EPIC 7 tab without code changes
- A new, separate tab exists for EPIC 7 real-provider setup workflow
- Project compiles successfully after the changes

## Next Slice

- Replace `RealNFeService` stub with concrete provider adapter implementation
- Improve secure certificate handling (avoid plain text entry/storage lifecycle) and provider-specific validation
- Persist and surface failure-path audit entries for rejected/errored emissions
- Add automated tests for provider switching from JavaFX controller actions
