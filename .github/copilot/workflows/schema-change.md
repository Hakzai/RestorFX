# Schema Change Workflow

## Goal
Apply database changes safely.

---

## Steps

### 1. Define Change
- Add column / modify structure

---

### 2. Generate Migration
Use agent: schema-migration-agent

---

### 3. Validate Impact
Use agent: regression-reviewer

---

### 4. Apply Changes in Code
Use agent: feature-evolution-agent

---

### 5. Final Check
Use agent: pre-commit-reviewer

---

## Rules

- Never delete data silently
- Prefer additive changes

---

## Done Criteria

- Schema updated safely
- Data preserved
- System still works