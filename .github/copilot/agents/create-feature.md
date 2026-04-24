# Agent: Create Feature

## Goal
Create a complete feature end-to-end using existing skills and project rules.

## Input
- Feature name
- Fields (name + type)

Example:
Feature: MenuItem  
Fields: name (String), price (double), description (String)

---

## Execution Plan

Follow ALL steps in order:

### Step 1 - Validate Context
- Read:
  - architecture.md
  - engineering-guidelines.md
  - feature-development.md
  - tech-stack.md

---

### Step 2 - Define Entity
- Identify:
  - Entity name
  - Fields and types
- Ensure naming follows coding-standards.md

---

### Step 3 - Generate CRUD
Use skill: `crud-generator`

- Generate:
  - Model
  - Repository (JDBC)
  - Service
  - Controller
  - Basic UI

---

### Step 4 - Sync Database
Use skill: `sqlite-schema-sync`

- Generate or update SQL schema
- Ensure compatibility with existing tables

---

### Step 5 - Build UI
Use skill: `javafx-screen-builder`

- Improve layout if needed
- Ensure usability:
  - list
  - create/edit form
  - delete

---

### Step 6 - Dependencies
Use skill: `maven-dependency-manager`

- Add any missing dependencies
- Keep pom.xml clean

---

### Step 7 - Validate Architecture
Use skill: `refactor-to-layered`

- Ensure:
  - no business logic in controller
  - no DB access outside repository

---

### Step 8 - Generate Test Checklist
Use skill: `manual-test-checklist`

- Provide checklist for manual validation

---

## Rules

- DO NOT skip steps
- DO NOT create unused classes
- DO NOT overengineer
- ALWAYS prefer libraries over custom code
- KEEP everything minimal and working

---

## Output Format

1. Summary of what was created
2. Files generated (with paths)
3. SQL changes
4. Dependencies added
5. Manual test checklist

---

## Success Criteria

- Feature works end-to-end
- Data persists correctly
- UI is usable
- Code follows architecture rules