---
description: "Use when you need a minimal, safe workflow for fixing a bug quickly without broad refactoring."
---
# Hotfix Workflow

## Goal
Fix a bug quickly without breaking existing features.

---

## Steps

### 1. Identify Issue
- Describe bug clearly
- Provide failing scenario

---

### 2. Fix
Use agent: auto-fix

OR manual fix

---

### 3. Regression Check
Use agent: regression-reviewer

---

### 4. Validation
Use agent: pre-commit-reviewer

---

## Rules

- Minimal change only
- No refactoring
- No new features

---

## Done Criteria

- Bug fixed
- No new issues introduced