---
name: license-system-helper
description: "Use this skill for the license-system-helper workflow in this repository."
---
# Skill: License System Helper

## Goal
Implement and maintain license/trial system.

## Instructions

1. Create:
   - License model
   - LicenseService
   - MachineId generator
   - License file handler (JSON)

2. Features:
   - Trial (30 days)
   - Validation by date
   - Machine binding

3. Rules:
   - Use Jackson for JSON
   - Store file locally (/data/license.dat)

4. Output:
   - Working license system
   - Easy to integrate at startup