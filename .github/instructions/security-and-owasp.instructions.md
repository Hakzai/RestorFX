---
description: "Security guidance for RestorFX covering input validation, prepared statements, secrets, NFe integration, and safe error handling."
applyTo: "**/*.java, **/*.fxml, **/*.sql, **/pom.xml"
---

# Security and OWASP Guidance

## Goal

Keep the RestorFX desktop application safe by default. Prefer simple controls that fit Java 8, JavaFX, SQLite, and JDBC.

## Core Rules

- Validate all user input before it reaches the service or repository layer.
- Use prepared statements for every SQL query.
- Never build SQL with string concatenation.
- Never log secrets, tokens, certificate passwords, or private key material.
- Keep stack traces out of user-facing dialogs.
- Log technical details with SLF4J for diagnostics.
- Keep external calls inside the integration layer.
- Prefer existing libraries for cryptography, XML, JSON, and HTTP.

## Data Access

- Use JDBC only.
- Always use `PreparedStatement` for queries and updates.
- Validate IDs, quantities, amounts, and date ranges before persistence.
- Treat file paths, URLs, and certificate aliases as untrusted input.

## Authentication and Secrets

- Store secrets outside source control.
- Load credentials from environment variables, protected local files, or the JRE keystore when appropriate.
- Never hardcode passwords, API keys, or certificate passwords.
- If the feature uses NFe, keep certificate handling isolated in the integration layer.

## XML and External Integration

- Never parse XML with unsafe defaults.
- Prefer a proven XML library and disable external entities.
- Validate outbound payloads before sending them to a provider.
- Log request and response metadata, not sensitive payload contents.

## JavaFX Safety

- Never block the JavaFX application thread with long-running I/O or network work.
- Show user-friendly error dialogs.
- Keep technical error details in logs only.

## Error Handling

- Catch expected exceptions close to the failure point.
- Wrap low-level exceptions in domain-specific messages when they cross layers.
- Do not swallow exceptions silently.

## Review Checklist

- SQL uses parameters.
- Sensitive data is not printed or logged.
- UI errors are readable.
- Integration code is isolated.
- XML and file input are validated.