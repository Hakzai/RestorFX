---
description: "Use when you want to create a new feature from scratch and run it through create, review, fix, and pre-commit validation steps."
---
# Full Cycle Workflow

## Goal
Create a new feature from scratch and deliver it production-ready.

---

## Steps

### 1. Create Feature
Use agent: create-feature

Input:
- Feature name
- Fields

---

### 2. Review Code
Use agent: code-reviewer

- Identify issues
- Classify severity

---

### 3. Fix Issues
Use agent: auto-fix

- Fix HIGH issues
- Fix simple MEDIUM issues

---

### 4. Quality Gate
Use agent: pre-commit-reviewer

---

## Loop

If REJECTED:

1. Use auto-fix
2. Run pre-commit-reviewer again

Max 3 iterations

---

## Done Criteria

- Feature works end-to-end
- Passes pre-commit-reviewer
- No HIGH issues
- UI functional
- Data persists correctly