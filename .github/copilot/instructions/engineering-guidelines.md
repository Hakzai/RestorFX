# Engineering Guidelines

You are working on a Java 8 desktop application (JavaFX + SQLite) following a layered architecture.

## CRITICAL RULES

### 1. DO NOT reinvent the wheel

- Always prefer well-known, stable libraries over custom implementations.
- Before implementing anything manually, check if a library already solves it.

#### 1.1 Before implementing any feature:

1. List possible libraries that solve the problem.
2. Choose one.
3. Justify the choice briefly.
4. Then implement.

### 2. Preferred libraries (use these by default when applicable):

- **JSON:** Jackson or Gson
- **Database access:** JDBC (simple) or lightweight helper (no ORM for now)
- **Logging:** SLF4J
- **Date handling:** `java.time` (native)
- **Validation:** Apache Commons / Hibernate Validator (if needed)

### 3. Avoid building custom implementations for:

- JSON parsing/serialization
- Logging frameworks
- Database connection pooling (use existing solutions if needed)
- Encryption/hashing (use standard Java libs)

### 4. Keep the system SIMPLE

- No unnecessary abstractions
- No generic frameworks
- No premature optimization
- No microservices

### 5. Follow the defined architecture strictly

- `controller → service → repository`
- No layer skipping
- No business logic inside UI

### 6. When in doubt

- Choose the simplest working solution
- Prefer readability over cleverness

### 7. Code generation rules

- Generate only what is necessary for the current feature
- Do not create unused classes
- Do not anticipate future features unless explicitly asked

### 8. External integrations (like NFe)

- ALWAYS prefer existing libraries
- NEVER attempt to implement protocols from scratch

### 9. Dependencies

- If a library is needed, explicitly suggest adding it to pom.xml
- Do not simulate libraries with custom code

### 10. Output style

- Clean, minimal, production-like code
- No placeholders like "TODO implement later" unless requested

---

## GOAL

Deliver a working, minimal, maintainable prototype as fast as possible.

---