# Feature Evolution Workflow

## Goal
Modify an existing feature safely.

---

## Steps

### 1. Evolve Feature
Use agent: feature-evolution-agent

Input:
- Existing feature
- Requested change

---

### 2. Check Database Impact
Use agent: schema-migration-agent

---

### 3. Regression Analysis
Use agent: regression-reviewer

---

### 4. Fix Issues (if needed)
Use agent: auto-fix

---

### 5. Final Validation
Use agent: pre-commit-reviewer

---

## Done Criteria

- Existing behavior preserved
- New feature works
- No regression risk HIGH