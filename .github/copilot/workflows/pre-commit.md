# Pre-Commit Workflow

## Goal
Ensure code is safe before committing.

---

## Steps

### 1. Review
Use agent: code-reviewer

---

### 2. Fix
Use agent: auto-fix

---

### 3. Gate
Use agent: pre-commit-reviewer

---

## Rule

Do NOT commit if:
- REJECTED
- HIGH issues exist

---

## Done Criteria

- APPROVED by pre-commit-reviewer