# Approach B: OpenELIS-Global-2 as New System Integration

**Date:** 2026-02-17
**Prepared by:** Engineering Team
**Status:** Draft — Alternative to [migration-analysis-openelis-global-v3.md](migration-analysis-openelis-global-v3.md)

---

## 1. Executive Summary

> *"This is not a usual upgrade — it's another system integration. It's a completely different system, and we are not looking for feature parity or bringing in changes done in Bahmni to OEG2."* — SME

This document presents an alternative framing to the migration analysis. Rather than treating the move from Bahmni OpenELIS to OpenELIS-Global-2 (OE-Global-2) as a **migration** (porting features, assessing gaps, carrying forward customizations), this approach treats it as a **system replacement and integration** project:

- **OE-Global-2 is adopted as-is** — it is a mature, modern LIS with its own features, UI, and workflows
- **No feature porting** — Bahmni customizations (77 `org.bahmni` files, 132 new + 1,522 modified `us.mn.state.health.lims` files) are **retired**, not migrated
- **The work is integration** — connecting OE-Global-2 to the Bahmni ecosystem (OpenMRS, Odoo) via FHIR
- **OE-Global-2's native capabilities are accepted** — where it differs from the Bahmni fork, OE-Global-2's approach is adopted rather than customized to match legacy behavior

**Why this framing matters:** The migration framing (Approach A) leads to questions like "does OE-Global-2 have an equivalent of our CSV upload feature?" and creates pressure to port or rebuild Bahmni-specific features. The integration framing asks instead: "what does OE-Global-2 need to exchange data with Bahmni's OpenMRS?" — a much smaller, more focused scope.

---

## 2. What Changes — and What Doesn't

### 2.1 What Gets Retired (Bahmni OpenELIS)

The entire Bahmni OpenELIS codebase is retired, including:

| Category | Scope | Action |
|---|---|---|
| `org.bahmni.feed.openelis` — AtomFeed integration | 5,925 LOC, bi-directional sync with OpenMRS | **Retired.** Replaced by OE-Global-2's native FHIR integration |
| `org.bahmni.openelis.domain` — REST/JSON APIs | 503 LOC, patient/accession JSON endpoints | **Retired.** Replaced by OE-Global-2's REST + FHIR endpoints |
| `us.mn.state.health.lims` — Bahmni additions | 132 new files (dashboard, CSV upload, health center, etc.) | **Retired.** OE-Global-2 has its own equivalents or the features are not carried forward |
| `us.mn.state.health.lims` — Bahmni modifications | 1,522 modified original files | **Retired.** These are modifications to the old OpenELIS codebase which no longer applies |
| Bahmni-specific DB tables | 13 tables (AtomFeed infra, health center, sample source, etc.) | **Retired.** Not needed in OE-Global-2 |
| Struts/JSP frontend | All JSP pages, Struts actions, Tiles definitions | **Retired.** OE-Global-2 has a React frontend |

### 2.2 What Gets Adopted (OE-Global-2 As-Is)

OE-Global-2 is adopted as a complete, standalone LIS with these capabilities already built in:

| Capability | OE-Global-2 Native Feature |
|---|---|
| Lab dashboard | `PatientDashBoardProvider`, `DashBoardMetrics` — richer than Bahmni's (turnaround time, rejection tracking) |
| Sample registration & tracking | `SamplePatientEntryService`, `SampleEditService`, accession number generation |
| Result entry & validation | Multi-level validation (technical + biological), `AccessionValidationController` |
| Patient management | Full CRUD + FHIR Patient resource mapping |
| Organization/facility management | `Organization` module with types (replaces Bahmni's health center) |
| Sample nature/source | `SampleNature` form field + `DisplayListService` |
| Provider tracking | FHIR Practitioner support + `ProviderImportService` |
| Analyzer integration | Plugin-based framework for automated result import |
| Reporting | JasperReports + newer report types |
| Referral management | Inter-lab sample referral (new capability) |
| Quality assurance | Non-conformity tracking, quality control |
| Barcode/label printing | Sample label generation |
| Odoo integration | Built-in `org.openelisglobal.odoo` package |
| FHIR integration | HAPI FHIR 6.6.2 (R4) — Patient, ServiceRequest, Task, DiagnosticReport, Observation |

**Users will use OE-Global-2's React UI**, not Bahmni Apps, for all lab workflows. This is a change from the current setup where lab data is sometimes accessed via Bahmni Apps.

### 2.3 What Gets Built (Integration Work)

The only new development is the **FHIR-based integration** between OE-Global-2 and Bahmni's OpenMRS:

| Integration Point | Direction | Description |
|---|---|---|
| **Lab order flow** | OpenMRS → OE-Global-2 | ServiceRequest raised in Bahmni triggers a FHIR Task that OE-Global-2 picks up, creating an electronic order |
| **Patient sync** | OpenMRS → OE-Global-2 | Patient demographics synced via FHIR Patient resource |
| **Result publishing** | OE-Global-2 → OpenMRS | Validated results published as FHIR DiagnosticReport + Observation, consumed by OpenMRS |
| **Reference data sync** | OpenMRS → OE-Global-2 | Tests, panels, sample types mastered in OpenMRS must be synced to OE-Global-2 |

Plus supporting work:

| Work Item | Description |
|---|---|
| **Bahmni default configuration** | Define a standard OE-Global-2 config package harmonized with OpenMRS test/panel/sample type definitions |
| **Data migration tooling** (for existing installations) | Productized tool to migrate core data (patients, samples, results) from old Bahmni OpenELIS to OE-Global-2 |
| **Docker Compose integration** | Add OE-Global-2's 5 containers to the Bahmni Docker Compose stack |

---

## 3. Integration Architecture

### 3.1 Current State (AtomFeed)

```
┌─────────────┐  AtomFeed   ┌─────────────────┐
│   OpenMRS   │────────────▶│  Bahmni OpenELIS │
│  (Bahmni)   │◀────────────│  (Struts/JSP)    │
└──────┬──────┘  AtomFeed   └─────────────────┘
       │
       │ AtomFeed
       ▼
┌─────────────┐
│    Odoo     │  (billing — no direct ELIS connection)
└─────────────┘
```

### 3.2 Target State (FHIR)

```
┌─────────────┐   FHIR R4   ┌──────────────────────────────────┐
│   OpenMRS   │─────────────▶│  OpenELIS-Global-2               │
│  (Bahmni)   │◀─────────────│  ┌────────┐ ┌────────┐ ┌──────┐│
│             │  FHIR R4     │  │ webapp │ │FHIR API│ │React ││
│  FHIR2      │              │  └────────┘ └────────┘ └──────┘│
│  module     │              │  ┌────────┐ ┌────────┐         │
│             │              │  │  DB    │ │ proxy  │         │
└──────┬──────┘              └──────────────────────────────────┘
       │
       │ (unchanged — AtomFeed or FHIR, independent of ELIS)
       ▼
┌─────────────┐
│    Odoo     │  (billing — still no direct ELIS connection)
└─────────────┘
```

### 3.3 FHIR Resource Mapping

| Workflow | FHIR Resource | Direction | Notes |
|---|---|---|---|
| Lab order placed | `ServiceRequest` + `Task` | OpenMRS → OE-Global-2 | OE-Global-2 polls FHIR Tasks via `FhirApiWorkFlowServiceImpl` |
| Patient demographics | `Patient` | OpenMRS → OE-Global-2 | Extracted from Task context; synced to OE-Global-2's patient registry |
| Results validated | `DiagnosticReport` + `Observation` | OE-Global-2 → OpenMRS | Published by `FhirTransformService`; OpenMRS FHIR2 module consumes them |
| Reference data | `ActivityDefinition` / `PlanDefinition` (TBD) | OpenMRS → OE-Global-2 | **Gap — needs architectural decision** (see Section 4) |

### 3.4 What OE-Global-2 Already Has for FHIR Integration

OE-Global-2 has a complete FHIR integration layer — this is not greenfield work:

| Component | Purpose |
|---|---|
| `FhirApiWorkFlowServiceImpl` | Polls FHIR server for Tasks, processes lab orders |
| `TaskWorker` + `TaskInterpreter` | Interprets FHIR Task → ServiceRequest → ElectronicOrder |
| `FhirTransformService` | Transforms OE-Global-2 results into FHIR DiagnosticReport + Observation |
| `FhirConfig.java` | FHIR server connection configuration |
| HAPI FHIR Client (`IGenericClient`) | Standards-based FHIR client library |
| `@Scheduled` polling | Spring-managed scheduled tasks (replaces Quartz + AtomFeed polling) |

### 3.5 What Bahmni Needs on the OpenMRS Side

| Requirement | Current Status | Work Needed |
|---|---|---|
| FHIR2 module | [Exists](https://github.com/Bahmni/openmrs-module-fhir2Extension) | Validate it supports the full lab order/result FHIR workflow OE-Global-2 expects |
| ServiceRequest creation | Currently creates AtomFeed events | Must create FHIR ServiceRequest + Task resources that OE-Global-2 can consume |
| DiagnosticReport consumption | Currently consumes AtomFeed result events | Must consume FHIR DiagnosticReport + Observation from OE-Global-2 |
| Reference data as FHIR resources | Not currently exposed as FHIR | Tests/panels/sample types need to be exposed as FHIR resources (or alternative sync mechanism) |

**Note:** The FHIR integration is **not just configuration** (per SME). Angshuman Sarkar's input is needed to assess the complexity on the OpenMRS/Bahmni side.

---

## 4. Key Technical Decisions Needed

### 4.1 Reference Data Sync (Biggest Open Question)

**Problem:** Lab tests, panels, and sample types are mastered in OpenMRS and currently synced to OpenELIS via AtomFeed. OE-Global-2 manages test configuration locally and has no inbound sync mechanism.

**Options:**

| Option | Description | Pros | Cons |
|---|---|---|---|
| **A. FHIR-based sync** | OpenMRS exposes test catalog as FHIR resources (ActivityDefinition/PlanDefinition); build sync consumer in OE-Global-2 | Standards-based; future-proof | Requires work on both sides; FHIR representation of lab test catalog is not straightforward |
| **B. Change mastering model** | Manage test config directly in OE-Global-2; sync to OpenMRS instead of the reverse | OE-Global-2 is designed for this; eliminates the gap | Changes the current Bahmni workflow where tests are configured in OpenMRS |
| **C. Periodic import** | Export from OpenMRS, import into OE-Global-2 via its existing import mechanisms or scripts | Simplest implementation | Not real-time; manual or batch process; fragile |

**Recommendation:** Option B (change mastering model) is the most architecturally clean, but requires buy-in from the Bahmni team on changing the test configuration workflow. Needs discussion.

### 4.2 Data Migration for Existing Installations

**Problem:** IOM starts fresh, but other installations have production data in Bahmni OpenELIS that needs to move to OE-Global-2.

**Approach:** Build a productized migration tool (not a one-off script) that:
1. Reads from Bahmni OpenELIS's `clinlims` schema
2. Maps core entities (patients, samples, analyses, results, tests) to OE-Global-2's evolved schema
3. Handles Bahmni-specific data (health centers → organizations, sample sources → sample natures)
4. Provides validation and rollback capabilities
5. Is packaged as a repeatable, testable tool

### 4.3 Bahmni Apps Lab Module

**Question:** Does Bahmni Apps (the EMR frontend) currently have a lab module that directly calls OpenELIS REST APIs?

If yes, this module either:
- **Gets retired** — lab users switch entirely to OE-Global-2's React UI
- **Gets rewritten** — to consume FHIR endpoints from OE-Global-2 instead of the old REST APIs

The SME directive to "assume FHIR endpoints" suggests the former — lab workflows move to OE-Global-2's own UI, and Bahmni Apps only needs to see lab results (via FHIR DiagnosticReport).

---

## 5. Scope Comparison: Integration Approach vs Migration Approach

| Aspect | Approach A: Migration | Approach B: Integration (this document) |
|---|---|---|
| **Framing** | Port features from old system to new | Replace old system; integrate new system |
| **Bahmni customizations** | Triage 132 new + 1,522 modified files; port what's needed | Retire all; OE-Global-2's native features accepted as-is |
| **Feature parity** | Gap analysis against each Bahmni feature | Not a goal; OE-Global-2 is adopted with its own feature set |
| **CSV bulk upload** | Identified as gap, may need rebuilding | Not carried forward unless OE-Global-2 community adds it |
| **Custom test status types** | May need porting if actively used | Not carried forward; use OE-Global-2's standard status model |
| **Lab dashboard** | Compare Bahmni's dashboard to OE-Global-2's | Use OE-Global-2's dashboard as-is |
| **REST API adapters** | May need backward-compatible APIs | Not needed; FHIR-first approach |
| **Lab UI** | Potentially Bahmni Apps for some workflows | OE-Global-2's React UI for all lab workflows |
| **Development scope** | FHIR integration + gap development + porting | FHIR integration + reference data sync + data migration tooling |
| **Risk of scope creep** | High — every Bahmni difference becomes a potential "requirement" | Low — OE-Global-2 accepted as-is, integration scope is bounded |
| **Estimated effort** | 12-20 weeks + 4-6 weeks for data migration | 8-14 weeks (see Section 6) |

---

## 6. Effort Estimate

### 6.1 For IOM (Fresh Deployment)

| Work Item | Estimated Effort | Notes |
|---|---|---|
| **FHIR integration: Lab orders (OpenMRS → OE-Global-2)** | 3-4 weeks | Configure OE-Global-2 FHIR polling; validate/enhance Bahmni FHIR2 module for ServiceRequest/Task creation. Complexity TBD with Angshuman Sarkar |
| **FHIR integration: Results (OE-Global-2 → OpenMRS)** | 2-3 weeks | Configure OE-Global-2 result publishing; validate OpenMRS FHIR2 module consumes DiagnosticReport/Observation |
| **Reference data sync** | 2-4 weeks | Depends on architectural decision (Section 4.1). Option B (change mastering) is faster; Option A (FHIR sync) takes longer |
| **Bahmni default configuration** | 1-2 weeks | Define test catalog, panels, sample types, organization types, form field settings |
| **Docker Compose integration** | 1 week | Add OE-Global-2 containers to Bahmni stack; networking, proxy, SSL |
| **End-to-end testing** | 2-3 weeks | Full lab workflow: order → sample → result → validation → report back to OpenMRS |
| **Total (IOM)** | **8-14 weeks** | |

### 6.2 Additional: Data Migration Tooling (For Existing Installations)

| Work Item | Estimated Effort | Notes |
|---|---|---|
| Schema mapping analysis | 1-2 weeks | Map Bahmni OpenELIS schema to OE-Global-2 schema |
| Migration tool development | 3-4 weeks | Productized, repeatable tool with validation |
| Testing with production data | 2-3 weeks | Multiple test runs with real data from existing installations |
| **Total (data migration)** | **6-9 weeks** | Can be done in parallel with or after IOM deployment |

### 6.3 Phased Rollout

Per SME direction, the migration follows a phased approach:

| Phase | Scope | Duration |
|---|---|---|
| **Phase 1: PoC** | Deploy OE-Global-2 standalone; validate FHIR integration with test OpenMRS instance | 3-4 weeks |
| **Phase 2: IOM integration** | Full Bahmni + OE-Global-2 integration for IOM (fresh deployment) | 5-10 weeks |
| **Phase 3: Data migration tooling** | Build and test migration tool for existing installations | 6-9 weeks (can overlap) |
| **Phase 4: Rollout to existing installations** | Migrate existing deployments using the tooling | Per-installation (1-2 weeks each) |

---

## 7. Risks

| Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|
| FHIR integration is more complex than expected | Medium-High | High | SME already flagged this; involve Angshuman Sarkar in Phase 1 PoC |
| Reference data sync architecture decision delays | Medium | Medium | Force a decision in Phase 1; Option C (periodic import) as fallback |
| OE-Global-2 UI/workflow doesn't meet lab user expectations | Medium | Medium | Early user testing in Phase 1 PoC; accept that some workflow differences are expected |
| Bahmni FHIR2 module needs significant enhancement | Medium | High | Assess in Phase 1 PoC; budget for OpenMRS-side development |
| Data migration tool complexity for diverse installations | Medium | Medium | Start with one reference installation; iterate |
| OE-Global-2 community version instability (develop branch) | Medium | Medium | Pin to a stable release tag; contribute fixes upstream |

---

## 8. What We Lose (Trade-offs)

Being explicit about what this approach does **not** carry forward:

| Feature | Status | Rationale |
|---|---|---|
| CSV bulk upload of patients/samples/results | **Not carried forward** | Use OE-Global-2's native data entry or FHIR import instead |
| Custom test status types | **Not carried forward** | Use OE-Global-2's standard analysis status lifecycle |
| Bahmni-specific lab dashboard JSON API | **Not carried forward** | Use OE-Global-2's native React dashboard |
| Bahmni Apps integration for lab display | **Replaced** | Lab results visible in OpenMRS via FHIR DiagnosticReport; lab workflows in OE-Global-2 UI |
| AtomFeed-based event sync | **Replaced** | FHIR-based integration |
| Any Bahmni-specific modifications to core OpenELIS code | **Not carried forward** | Accept OE-Global-2's implementation |

If any of these features turn out to be critical after further analysis, they can be contributed as features to the OE-Global-2 community project rather than maintained as Bahmni-specific forks.

---

## 9. Open Questions

| # | Question | Needed For | Owner |
|---|---|---|---|
| 1 | What is the exact FHIR integration complexity? | Effort estimate accuracy | Angshuman Sarkar |
| 2 | Which reference data sync approach to adopt? (Section 4.1) | Architecture decision | Team + Angshuman |
| 3 | Does Bahmni Apps have a lab module that directly calls OpenELIS APIs? | Scope of Bahmni-side changes | SME |
| 4 | What data is sent back to OpenMRS when results are validated? | Integration completeness | SME |
| 5 | Is sample source feature required? | Configuration scope | SME |
| 6 | Are there installations with configurations that need special handling? | Data migration tool scope | SME |

---

## 10. Conclusion

Treating this as a **system integration** rather than a **migration** fundamentally simplifies the project:

- **Scope is bounded** — the work is FHIR integration + reference data sync + data migration tooling, not feature-by-feature gap analysis and porting
- **No fork maintenance** — OE-Global-2 is used as-is, benefiting from upstream community development
- **Modern stack** — Java 21, Spring 6, React replaces Java 7/8, Struts 1.x, JSP without any porting effort
- **Estimated effort is lower** — 8-14 weeks for IOM vs 12-20 weeks in the migration approach
- **Risk of scope creep is lower** — the integration framing avoids the trap of "we also need to port X" for every Bahmni-specific feature

The key risk is that the FHIR integration is more complex than it appears (per SME). Phase 1 (PoC) should validate this with Angshuman Sarkar's involvement before committing to the full timeline.

---

*This document presents Approach B (System Integration) as an alternative to [Approach A (Migration Analysis)](migration-analysis-openelis-global-v3.md). Both documents should be reviewed together when making the strategic decision.*
