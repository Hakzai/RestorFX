---
name: refactor-to-layered
description: "Use this skill for the refactor-to-layered workflow in this repository."
---
# Skill: Refactor to Layered Architecture

## Goal
Fix architecture violations.

## Instructions

1. Analyze code:
   - Detect logic in wrong layer

2. Refactor:
   - Move business logic → service
   - Move DB access → repository
   - Clean controller

3. Rules:
   - Follow architecture.md strictly

4. Output:
   - Clean layered structure
   - No rule violations