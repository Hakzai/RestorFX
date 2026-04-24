# Skill: SQLite Schema Sync

## Goal
Ensure database schema matches application models.

## Instructions

1. Analyze entity classes
2. Generate SQL schema:
   - CREATE TABLE
   - INDEXES
   - FOREIGN KEYS

3. Rules:
   - Use SQLite syntax
   - Use INTEGER PRIMARY KEY AUTOINCREMENT
   - Use REAL for monetary values

4. Output:
   - SQL script
   - Migration-safe (IF NOT EXISTS)

## Extra
Suggest improvements if schema is inconsistent