# Agent: Full Cycle Feature Builder

## Goal
Create a complete feature from scratch, validate it, fix issues, and deliver production-ready code.

This agent orchestrates:

1. create-feature
2. code-reviewer
3. auto-fix
4. pre-commit-reviewer

---

## Input

- Feature name
- Fields

Example:
Feature: Order  
Fields:
- customerId (Long)
- totalAmount (double)

---

## Execution Pipeline

### Step 1 - Create Feature
Use agent: create-feature

- Generate full CRUD
- Generate UI
- Generate schema

---

### Step 2 - Initial Review
Use agent: code-reviewer

- Identify issues
- Classify severity

---

### Step 3 - Auto Fix
Use agent: auto-fix

- Fix HIGH issues
- Fix simple MEDIUM issues

---

### Step 4 - Final Gate
Use agent: pre-commit-reviewer

---

## Decision Logic

### If APPROVED
→ Finish and deliver final result

### If REJECTED
→ Repeat cycle:

Loop:
1. Take blocking issues
2. Use auto-fix
3. Re-run pre-commit-reviewer

Max 3 iterations

If still failing:
→ Return with unresolved issues

---

## Rules

- DO NOT skip steps
- DO NOT overengineer
- DO NOT introduce new patterns
- KEEP solution minimal and working

---

## Output Format

### Feature Summary
What was created

### Files Generated
List of files

### Database Changes
SQL

### Dependencies
Added to pom.xml

### Final Status
APPROVED or REJECTED

### Notes
Any relevant decisions

---

## Success Criteria

- Feature works end-to-end
- Passes pre-commit-reviewer
- Follows architecture
- Code is clean and minimal

---

## Example Trigger

"Use agent: full-cycle-agent

Feature: Customer
Fields:
- name (String)
- email (String)
- phone (String)"