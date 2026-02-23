# Plan Maintenance Guide

*Instructions for updating the integration plan and its supporting documents.*

---

## Core Principle

The integration plan is a **forward-looking action document**. It answers: "What do we need to do?" Everything else — rationale, evidence, resolved items, historical context — belongs in supporting documents.

---

## Document Roles

| Role | Contains | Does NOT contain |
|---|---|---|
| **Plan** (`bahmni-openelis-global2-integration-plan.md`) | What we need to do, current open questions, current risks | Completed work, resolved questions, justifications, code evidence |
| **Decisions log** (`docs/decisions-log.md`) | Resolved questions, decisions with rationale, research findings, code verification evidence | Action items, to-do lists |
| **Design documents** (e.g., mediator service design, architecture detail) | Technical design: how components work, data flows, schemas, APIs | Resolved design questions (move to decisions log), action items (those go in the plan) |
| **Detail pages** (current-flow, proposed-flow) | Reference diagrams and explanations | Action items |

---

## Rules for the Plan

### 1. No completed/resolved items (with one exception)

- **Open Questions table**: Only active, unresolved questions. When resolved, remove the row and record the resolution in the decisions log.
- **Phase checklists (Section 6)**: Keep completed steps with `[x]` checkboxes — the plan section shows the full picture of progress. Record any decision/finding in the decisions log as well.
- **Elsewhere in the plan**: No completed items, no strikethrough text, no "DONE" markers. If it's done and it's not in the Section 6 phase checklists, it doesn't belong in the plan.

### 2. No justifications or evidence

The plan says *what* to do, not *why*. Rationale, code references, SME quotes, and verification evidence belong in the decisions log.

- **Bad**: "Billing: no gap (resolved). Code verification confirmed: test catalog sync to Odoo comes from OpenMRS..."
- **Good**: Remove the item entirely. The resolution lives in the decisions log.

### 3. Risks and dependencies go in Open Questions

If a plan item depends on an unresolved question or risk, reference the open question number. Don't inline the risk description — keep the single source of truth in the Open Questions table.

### 4. Keep the "At a Glance" section clean

Quick comparison table + one-liner summaries only. No multi-sentence explanations. Link to detail pages for elaboration.

### 5. Keep descriptions short

- Open question rows: 1-2 sentences stating the question and what it blocks.
- Phase checklist items: short imperative actions, optionally linking to a detail page or open question.
- Repo analysis table: "None", "Changes needed — [brief what]", or similar. Details belong in per-repo analysis files.

### 6. Date updates

Update the header date when making substantive changes. Keep the status line brief.

---

## Update Procedure

When incorporating new information (SME meetings, code analysis, etc.):

### Step 1: Record findings in the decisions log

Add new decisions and/or research findings with:
- What was decided or discovered
- Evidence (code paths, SME quotes, verification method)
- Date and source

### Step 2: Update the plan

For each finding, determine its effect:

| Finding type | Action on plan |
|---|---|
| Open question resolved | Remove the row from Open Questions table |
| Open question partially clarified | Update the row in-place |
| New risk or dependency discovered | Add a new row to Open Questions table |
| Phase step completed | Mark with `[x]` in Section 6 checklists; also record in decisions log |
| Phase step modified | Update the step text |
| New work item discovered | Add a step to the appropriate phase |
| Blocker removed | Remove the blocker mention |
| New repo identified for changes | Add to the Repository Analysis table |

### Step 3: Update design documents (if applicable)

If the finding affects a technical design document (e.g., component design, data flow, schema, API contract), update that document. Apply the same rules: keep resolved questions out (move them to the decisions log), keep the design document focused on *current* design, not history.

### Step 4: Verify consistency

After edits, scan for:
- Stale references to removed open questions
- Completed items still in the plan
- Justification text that should be in the decisions log
- Cross-references between documents that no longer match

---

## Anti-Patterns

| Don't | Do |
|---|---|
| Mark plan items as "DONE" with strikethrough | Use `[x]` checkboxes in Section 6 phase checklists; remove elsewhere |
| Inline code evidence in the plan | Put evidence in decisions log |
| Keep resolved questions in the Open Questions table | Remove the row |
| Add narrative paragraphs in the comparison section | Track risks as open questions |
| Write long explanations in phase checklists | Short action items; link to detail pages |
| Duplicate information across documents | Single source of truth: plan = what, decisions log = why, design docs = how |
