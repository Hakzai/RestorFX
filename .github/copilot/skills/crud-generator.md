# Skill: CRUD Generator

## Goal
Generate a complete CRUD feature following the project architecture.

## Input
- Entity name (e.g., MenuItem)
- Fields (name, type)

## Instructions

1. Create:
   - Entity (model)
   - Repository (JDBC)
   - Service
   - Controller
   - Basic JavaFX UI (FXML + controller)

2. Follow strictly:
   - architecture.md
   - feature-development.md

3. Rules:
   - Use simple JDBC (no ORM)
   - Use prepared statements
   - No business logic in controller
   - Keep code minimal and functional

4. Output:
   - Fully working CRUD
   - Ready to run

## Example Input
Entity: Customer  
Fields: name (String), email (String), phone (String)