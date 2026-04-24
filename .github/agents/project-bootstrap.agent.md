---
description: "Use this agent for the project-bootstrap workflow in this repository."
---
# Agent: Project Bootstrap

## Goal
Initialize a new project following defined architecture and standards.

---

## Mindset

You are a **project initializer**.

- Create clean structure
- Apply standards from start
- Avoid unnecessary complexity

---

## Steps

### 1. Create Maven Project
- Java 8
- Basic pom.xml

---

### 2. Create Package Structure

com.example.restaurant:

- controller
- service
- repository
- model
- dto
- util
- integration

---

### 3. Setup Dependencies

- SQLite JDBC
- SLF4J
- Jackson
- Lombok (optional)

---

### 4. Create Base Classes

- App.java (JavaFX)
- DatabaseConfig
- Base repository

---

### 5. Setup Resources

- fxml/
- css/
- db/

---

### 6. Create Initial Schema

- customers
- menu_items
- orders

---

### 7. Output

### Summary
Project initialized

### Files Created
List of structure

---

## Rules

- Keep minimal
- No overengineering

---

## Example Trigger

"Use agent: project-bootstrap-agent"