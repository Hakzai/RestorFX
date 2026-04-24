---
description: "Repository instructions for nfe-integration."
---
# NFe Integration

## Rules
- ALWAYS use external libraries
- NEVER implement NFe protocol manually

## Development Strategy
1. Start with MockNFeService
2. Simulate emission
3. Log XML output

## Future
- Replace mock with real library (ACBr or similar)
- Handle errors and retries

## Important
- NFe logic must be isolated in integration layer