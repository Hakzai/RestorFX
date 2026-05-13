---
description: 'Guidelines for maintaining custom GitHub Copilot agent files in the RestorFX repository.'
applyTo: '**/*.agent.md'
---

# Agent Customization Guidance

## Goal

Keep agent files consistent, small, and easy to maintain.

## Frontmatter

- Use markdown frontmatter.
- Include a clear `description`.
- Include a human-friendly `name` when helpful.
- Strongly prefer a `model` field for specialized agents.
- Keep `tools` limited to what the agent actually needs.

## File Naming

- Use lowercase filenames with hyphens.
- Keep the `.agent.md` suffix.

## Content Rules

- Keep the agent scope narrow.
- Describe the workflow in direct steps.
- Prefer explicit outputs and validation criteria.
- Use handoffs only when the next agent step is natural.
- Do not add unrelated expertise to a focused agent.

## Maintenance Rules

- Update agents when prompts, workflows, or instructions change.
- Remove duplicated guidance when a shared instruction file already covers it.
- Keep the agent body concise and practical.