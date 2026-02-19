# Repository: bahmni-frontend

*Back to [Integration Plan](../../bahmni-openelis-global2-integration-plan.md)*

**Repo:** `Bahmni/bahmni-frontend`
**Local path:** `/Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/bahmni-frontend`
**Deep dive date:** 2026-02-19
**Changes required:** None for existing lab flow

---

## Role in Integration

Bahmni's frontend (micro-frontend architecture) is where doctors enter lab orders and where results are displayed. The integration plan states the UI is unchanged — this analysis verifies that claim.

---

## Verified Findings

### Current State of the Micro-frontend
`bahmni-frontend` is a Lerna monorepo with an evolving micro-frontend architecture. As of the deep dive, it contains:
- `bahmni-medication-app` — drug/medication orders ✅
- `bahmni-mf-app-shell` — app shell ✅
- `bahmni-design-system` — shared components ✅
- **Lab order UI component — does NOT exist yet** ❗

### Two Frontends in Bahmni
The architecture diagram (`archDiagWithRepoLinks.svg`) confirms Bahmni has two frontends:
1. **Bahmni Web (Angular)** → `openmrs-module-bahmniapps` — the legacy app, **where lab order entry and result display lives today**
2. **Bahmni Apps Frontend (React)** → `bahmni-frontend` (this repo) — newer micro-frontend, medication only so far

**The lab order UI analysis in this file is therefore incomplete.** See [openmrs-module-bahmniapps.md](openmrs-module-bahmniapps.md) for the actual lab UI analysis.

### How Lab Orders Are Created
The UI submits orders via:
```
POST /openmrs/ws/rest/v1/bahmnicore/bahmniencounter
```
Payload includes an `orders` array with `conceptUuid`, `orderTypeUuid`, `orderer`, etc. This is a Bahmni-core API — the UI has no direct dependency on which LIS is used.

### How Lab Results Are Displayed
The UI fetches results via:
```
GET /openmrs/ws/rest/v1/bahmnicore/labOrderResults?patientUuid=<UUID>
```
Results are served from OpenMRS observations. The UI is completely abstracted from the LIS — it only cares that observations exist in OpenMRS with the right structure.

### No Direct OpenELIS References
Zero references to OpenELIS endpoints, OpenELIS URLs, or OpenELIS-specific APIs found in the frontend codebase. The decoupling via Bahmni Core is total.

### API Pattern
All domain operations go through Bahmni-specific APIs:
```
/openmrs/ws/rest/v1/bahmnicore/*   ← domain operations
/openmrs/ws/rest/v1/*               ← OpenMRS standard lookups
```
Uses axios-based HTTP client (`services/axios.ts`).

---

## Changes Required

**None for the lab order/result flow.** As long as:
1. The mediator writes results back to OpenMRS as observations (via `/ws/fhir2/Observation` or similar)
2. Those observations are served correctly by `/bahmnicore/labOrderResults`

...the UI works without any modification.

### Future Consideration (out of scope for this migration)

The new micro-frontend does not yet have a lab order UI component. This is a separate future effort (not part of the OEG2 migration). Lab order entry continues to work through the legacy Bahmni apps.

If/when a lab order component is built for the micro-frontend, it should call the same `bahmnicore/bahmniencounter` endpoint — no LIS-specific changes needed.

---

## Open Questions (repo-specific)

| # | Question |
|---|---|
| 1 | Is the lab order entry UI (currently in legacy Bahmni apps) in scope for this migration, or is it out of scope? The integration plan says "Bahmni UI order entry stays the same" — confirm this means the legacy UI, not a new micro-frontend component. |
| 2 | The result display (`/bahmnicore/labOrderResults`) needs to return results correctly after the mediator writes them. Needs end-to-end verification in Phase 1b / Phase 5 testing. |

---

## Key Files

| File | Purpose |
|---|---|
| `packages/bahmni-medication-app/src/constants.tsx` | Shows API endpoint pattern used by micro-frontend apps |
| `packages/bahmni-medication-app/src/services/axios.ts` | HTTP client setup |
