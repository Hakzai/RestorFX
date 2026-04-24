---
description: "Repository instructions for library-selection."
---
# Library Selection Rules

## Core Rule
DO NOT reinvent the wheel

## Process
Before implementing anything:
1. Check if a library exists
2. Prefer well-known solutions
3. Only implement manually if necessary

## Preferred Libraries
- JSON → Jackson
- Logging → SLF4J
- Utilities → Apache Commons

## NEVER implement:
- JSON parser
- Logging system
- Encryption algorithms
- HTTP clients

## Dependencies
- Always suggest dependency in pom.xml
- Do not simulate libraries manually