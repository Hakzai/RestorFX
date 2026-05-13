---
description: 'Guidelines for maintaining custom GitHub Copilot prompt files in the RestorFX repository.'
applyTo: '**/*.prompt.md'
---

# Prompt Authoring Guidance

## Goal

Keep prompt files reusable, predictable, and easy to run in VS Code.

## Frontmatter

- Include `description`.
- Add `name` when the prompt should be easy to discover.
- Use `agent` when the prompt depends on a specific workflow agent.
- Add `model` only when the prompt benefits from a specific model.
- Keep `tools` small and task-specific.

## File Naming

- Use lowercase kebab-case filenames.
- Keep the `.prompt.md` suffix.

## Body Structure

- Start with a clear heading.
- Explain the mission first.
- List inputs and preconditions.
- Describe the workflow step by step.
- Specify output format and validation expectations.

## Maintenance Rules

- Keep prompts aligned with current agent names.
- Remove stale references to deleted files or workflows.
- Keep instructions short and direct.
- Prefer reusable workflow steps over duplicated prose.