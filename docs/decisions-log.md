# Decision Log: Bahmni + OE-Global-2 Integration

*Back to [Integration Plan](../bahmni-openelis-global2-integration-plan.md)*

This log captures key decisions and research findings made during the design process.

---

## Decisions

| # | Decision | Rationale | Source |
|---|---|---|---|
| 1 | **Architecture: Simplified** — mediator pushes directly to OE-Global-2's FHIR store. No OpenHIM/SHR. | OpenHIM is a heavyweight interop layer for HL7v2/CDA translation — unnecessary for pure FHIR. Halves container count (7 vs 12). Path to Full OpenHIE is additive if needed later. | SME meeting (2026-02-18) |
| 2 | **Custom mediator service** instead of `openmrs-module-labonfhir` | Consistent with Bahmni's existing integration patterns (ERP/PACS — standalone microservice). Allows backward compatibility with old OpenELIS. | SME meeting (2026-02-18) |
| 3 | **Master data ownership:** OpenMRS for test definitions, OEG2 for reference ranges | Reference ranges vary by ethnicity, location — lab systems handle this better. Test definitions are mastered where orders originate. | SME meeting (2026-02-18) |
| 4 | **Patient sync: immediate** on creation, not on-demand with orders | Some doctors order on paper — patient must already exist in the lab. Reverse sync (lab → OpenMRS) deferred. | SME meeting (2026-02-18) |
| 5 | **Data migration: not required** | Existing installations keep old OpenELIS. New installations use OEG2. Patient migration via event replay script. Old results stay in old system. | SME meeting (2026-02-18) |
| 6 | **LOINC codes: Bahmni will be updated** | OEG2 requires LOINC codes for test matching. Bahmni's test catalog will be updated with LOINC codes as needed. | SME meeting (2026-02-18) |

## Research Findings

Key findings from community research and code analysis that informed the decisions above.

| Finding | Source |
|---|---|
| FHIR2 module alone doesn't create Tasks — an active bridge is required to push FHIR bundles to the lab | [Community discussion](https://talk.openelis-global.org/t/integration-with-openmrs-over-fhir/1702) |
| Exchange between OpenMRS and OE-Global-2 is purely FHIR R4 — no HL7v2 | [Moses Mutesasira](https://talk.openelis-global.org/t/integration-with-openmrs-over-fhir/1702/2) |
| OE-Global-2 matches tests by LOINC codes only — no fallback to custom codes | Code analysis |
| Test methods (PCR, culture, etc.) are selected at execution time by the lab tech, not at order time. LOINC mapping is test-level, not method-level. | [Community discussion](https://talk.openelis-global.org/t/openelis-global-capability-for-selecting-a-specific-method-for-a-given-order/1691) |
| A working reference implementation exists ([DIGI-UW/openelis-openmrs-hie](https://github.com/DIGI-UW/openelis-openmrs-hie)) with Docker Compose | GitHub |
| Reference implementation uses `openmrs-module-labonfhir` which detects orders via JMS events (instant) and returns results via polling | Code analysis |
| OE-Global-2's core data model hasn't changed drastically from old OpenELIS | SME meeting (2026-02-18) |
