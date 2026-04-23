# Coding Standards

## Naming
- Classes: PascalCase
- Methods: camelCase
- Variables: camelCase
- Packages: lowercase

## Structure
- One responsibility per class
- Keep methods small and readable

## Best Practices
- Prefer composition over inheritance
- Avoid static mutable state
- Avoid premature abstraction

## DTO vs Entity
- Entity = database representation
- DTO = UI or data transfer

## Logging
- Use SLF4J
- Never use System.out.println

## Clean Code
- Remove unused code
- Avoid commented code blocks