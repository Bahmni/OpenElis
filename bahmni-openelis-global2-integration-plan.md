# Bahmni + OpenELIS-Global-2: Integration Plan

**Date:** 2026-02-17
**Status:** Draft
**Objective:** Replace Bahmni's OpenELIS fork with OpenELIS-Global-2 (OE-Global-2), integrated via FHIR.

> Supporting analysis (archived): [migration-analysis-openelis-global-v3.md](archive/migration-analysis-openelis-global-v3.md), [openelis-global-2-integration-approach.md](archive/openelis-global-2-integration-approach.md)

---

## 1. Context

Bahmni currently ships a fork of OpenELIS (v3.1, circa 2013) as its LIS. This fork is integrated with OpenMRS via AtomFeed — a custom, polling-based event mechanism. The fork is on a dead-end tech stack (Java 7, Struts 1.x, JSP) and receives no upstream updates.

OpenELIS-Global-2 (OE-Global-2) is the actively maintained successor — a modern, full-featured LIS with native FHIR R4 support. **We are adopting it as-is**, not porting Bahmni customizations into it.

**The work is integration:** making OE-Global-2 and Bahmni's OpenMRS talk to each other via FHIR so that the clinical lab workflow functions end-to-end.

---

## 2. Integration Overview

### 2.1 What Crosses the Boundary

Only **four data flows** cross the OpenMRS ↔ OpenELIS boundary:

```
                        OpenMRS (Bahmni)                    OpenELIS-Global-2
                       ┌──────────────────┐                ┌──────────────────┐
                       │                  │  1. Lab Orders  │                  │
                       │  Clinician places │───────────────▶│  Lab receives    │
                       │  lab order        │  (FHIR Task +  │  order, creates  │
                       │                  │  ServiceRequest)│  sample          │
                       │                  │                 │                  │
                       │                  │  2. Results     │  Lab processes   │
                       │  Clinician views │◀───────────────│  sample,         │
                       │  lab results     │ (FHIR Diagnostic│  validates       │
                       │                  │  Report +       │  results         │
                       │                  │  Observation)   │                  │
                       │                  │                 │                  │
                       │                  │  3. Patient     │                  │
                       │  Patient         │───────────────▶│  Patient record  │
                       │  registered      │  (FHIR Patient) │  created/updated │
                       │                  │                 │                  │
                       │  Test catalog    │  4. Ref Data    │  Test catalog    │
                       │  (master)        │───────────────▶│  (consumer)      │
                       │                  │  (mechanism TBD)│                  │
                       └──────────────────┘                └──────────────────┘
```

**What does NOT cross the boundary:**
- Odoo/billing — Odoo connects to OpenMRS, not to OpenELIS. No change needed.
- Lab UI/workflows — lab users work entirely within OE-Global-2's React UI.
- Reporting — OE-Global-2 has its own reporting (JasperReports).

### 2.2 Current vs Target

| Data Flow | Current (AtomFeed) | Target (FHIR) |
|---|---|---|
| **Lab orders** | OpenMRS publishes AtomFeed event on ServiceRequest → OpenELIS polls feed, creates sample/analysis | OpenMRS creates FHIR Task + ServiceRequest → OE-Global-2 polls FHIR Tasks via `FhirApiWorkFlowServiceImpl`, creates electronic order |
| **Results** | OpenELIS publishes AtomFeed event on result validation → OpenMRS polls feed | OE-Global-2 publishes FHIR DiagnosticReport + Observation via `FhirTransformService` → OpenMRS FHIR2 module consumes them |
| **Patient sync** | OpenMRS publishes patient AtomFeed → OpenELIS syncs demographics | Patient data flows as part of the FHIR Task context (Patient resource reference) |
| **Reference data** | OpenMRS AtomFeed → OpenELIS syncs tests, panels, sample types | **TBD — see Section 4** |

---

## 3. FHIR Integration Detail

### 3.1 Flow 1: Lab Orders (OpenMRS → OE-Global-2)

**Trigger:** Clinician places a lab order in Bahmni.

**Current behavior:**
1. OpenMRS creates a ServiceRequest
2. An AtomFeed event is published
3. Bahmni OpenELIS polls the feed, fetches the order via REST, creates sample + analysis

**Target behavior:**
1. OpenMRS creates a FHIR `ServiceRequest` resource
2. OpenMRS creates a FHIR `Task` resource referencing the ServiceRequest (+ Patient)
3. OE-Global-2's `FhirApiWorkFlowServiceImpl` polls for new Tasks
4. `TaskWorker` + `TaskInterpreter` process the Task → creates an `ElectronicOrder` in OE-Global-2
5. Lab technician sees the order in OE-Global-2's React UI

**What OE-Global-2 already has:** The polling, Task interpretation, and ElectronicOrder creation are built in.

**What needs work on the OpenMRS/Bahmni side:**
- The Bahmni FHIR2 module ([openmrs-module-fhir2Extension](https://github.com/Bahmni/openmrs-module-fhir2Extension)) must create FHIR Task + ServiceRequest resources in the format OE-Global-2 expects
- Currently, Bahmni raises AtomFeed events — this needs to change to FHIR resource creation
- **This is the key area where Angshuman Sarkar's input is needed** — the SME flagged this is "not just configuration"

**Open questions:**
- Does the Bahmni FHIR2 module already create Task resources, or only ServiceRequest?
- What FHIR profile does OE-Global-2 expect for the Task (what fields, what references)?
- How does OE-Global-2 discover the FHIR server to poll? (Configuration: `FhirConfig.java`)

### 3.2 Flow 2: Results (OE-Global-2 → OpenMRS)

**Trigger:** Lab technician validates results in OE-Global-2.

**Current behavior:**
1. Results are validated in Bahmni OpenELIS
2. An AtomFeed event is published with result data
3. OpenMRS polls the feed, stores the lab result

**Target behavior:**
1. Results are validated in OE-Global-2
2. `FhirTransformService` creates FHIR `DiagnosticReport` + `Observation` resources
3. Resources are published to the FHIR server (OE-Global-2's external HAPI FHIR container)
4. OpenMRS FHIR2 module consumes the DiagnosticReport/Observation
5. Clinician sees lab results in Bahmni

**What OE-Global-2 already has:** The FHIR transform and publishing logic is built in.

**What needs work on the OpenMRS/Bahmni side:**
- OpenMRS FHIR2 module must be able to consume/subscribe to DiagnosticReport + Observation
- Bahmni's clinical display must render results from FHIR resources

**Open questions:**
- What is the subscription/polling mechanism? Does OpenMRS poll OE-Global-2's FHIR server, or does OE-Global-2 push to OpenMRS's FHIR endpoint?
- What fields from DiagnosticReport/Observation does Bahmni need to display results?
- How are result statuses (preliminary, final, corrected, amended) handled?

### 3.3 Flow 3: Patient Sync (OpenMRS → OE-Global-2)

**Trigger:** Patient registered or updated in Bahmni.

**Target behavior:** Patient data flows as part of the FHIR Task context — when a lab order Task is created, it references the Patient resource. OE-Global-2 resolves the Patient reference and creates/updates the patient in its registry.

**What OE-Global-2 already has:** Patient extraction from Task context is part of the existing FHIR workflow.

**Open question:** Is standalone patient sync needed (patient created but no lab order yet), or is patient-on-demand (synced when first lab order arrives) sufficient?

### 3.4 FHIR Server Topology

OE-Global-2 deploys its own HAPI FHIR server as a separate container. The integration architecture needs to decide:

| Option | Description | Implication |
|---|---|---|
| **Shared FHIR server** | OpenMRS and OE-Global-2 both read/write to the same FHIR server | Simpler; both systems see the same resources. Requires network access and access control. |
| **Separate FHIR servers** | Each system has its own FHIR server; resources are exchanged between them | More isolated; requires a sync/push mechanism between servers |
| **OpenMRS FHIR endpoint as source** | OE-Global-2 polls OpenMRS's FHIR2 module endpoint directly (no intermediate server for inbound flow) | Simplest for the order flow; OE-Global-2 already supports polling an external FHIR endpoint |

**Recommendation:** Option 3 for the order flow (OE-Global-2 polls OpenMRS's FHIR endpoint for Tasks) + OE-Global-2's own FHIR server for result publishing (OpenMRS polls it for DiagnosticReports). This aligns with OE-Global-2's existing architecture. **Needs validation in PoC.**

---

## 4. Reference Data Sync (Key Decision)

### 4.1 The Problem

Lab tests, panels, and sample types are **mastered in OpenMRS** and currently synced to OpenELIS via AtomFeed. OE-Global-2 manages its test catalog locally — it has no mechanism to receive test definitions from an external system.

Reference ranges are **managed only in OpenELIS** (per SME).

### 4.2 Options

**Option A: Keep OpenMRS as master, build FHIR-based sync to OE-Global-2**
- OpenMRS exposes test catalog as FHIR resources (e.g., `ActivityDefinition`, `PlanDefinition`, or `CatalogEntry`)
- Build a sync consumer in OE-Global-2 that reads these and creates/updates test configuration
- *Pros:* Preserves current workflow; standards-based
- *Cons:* FHIR representation of lab test catalogs is not well-established; requires development on both sides
- *Effort:* 3-4 weeks

**Option B: Move mastering to OE-Global-2**
- Test catalog (tests, panels, sample types) is managed directly in OE-Global-2's admin UI
- Sync to OpenMRS if needed (e.g., for order entry dropdowns)
- Reference ranges stay in OE-Global-2 (already the case)
- *Pros:* OE-Global-2 is designed for this; eliminates the sync gap entirely; simpler architecture
- *Cons:* Changes the Bahmni workflow for configuring lab tests; requires buy-in
- *Effort:* 1-2 weeks (mostly configuration and process change)

**Option C: Periodic batch import**
- Export test catalog from OpenMRS as a file; import into OE-Global-2 via scripts or its import APIs
- *Pros:* Simplest to implement
- *Cons:* Not real-time; manual or batch; fragile; not a long-term solution
- *Effort:* 1 week

**Recommendation:** Discuss with team. Option B is architecturally cleanest — the LIS should own its test catalog. Option A preserves the current workflow but is more work. Option C is a stopgap.

---

## 5. Deployment

### 5.1 Current Bahmni Stack (Relevant Containers)

```
bahmni/openmrs          — OpenMRS backend
bahmni/openmrs-db       — OpenMRS database
bahmni/openelis         — OpenELIS WAR (Tomcat 8, port 8052)  ← REPLACED
bahmni/openelis-db      — OpenELIS database                    ← REPLACED
bahmni/odoo             — Odoo (billing)
bahmni/odoo-db          — Odoo database
```

### 5.2 Target Stack

```
bahmni/openmrs          — OpenMRS backend (unchanged)
bahmni/openmrs-db       — OpenMRS database (unchanged)
openelisglobal-webapp   — OE-Global-2 Java backend             ← NEW
openelisglobal-database — OE-Global-2 PostgreSQL               ← NEW
external-fhir-api      — HAPI FHIR server                     ← NEW
openelisglobal-frontend — React SPA                            ← NEW
openelisglobal-proxy    — nginx reverse proxy                  ← NEW
bahmni/odoo             — Odoo (unchanged)
bahmni/odoo-db          — Odoo database (unchanged)
```

**Change:** 2 containers removed (old OpenELIS), 5 containers added (OE-Global-2). Net +3 containers.

**Work needed:**
- Integrate OE-Global-2's Docker Compose definitions into the Bahmni Docker Compose stack
- Configure networking so OE-Global-2 can reach OpenMRS's FHIR endpoint (and vice versa)
- SSL/proxy configuration for OE-Global-2's frontend
- Environment variable configuration (DB credentials, FHIR server URLs)

---

## 6. Data Migration (Future — Existing Installations)

> **Current client starts fresh — no data migration needed.** This section is a placeholder for when OE-Global-2 is rolled into a future Bahmni release and existing installations need to upgrade.

When OE-Global-2 becomes part of the standard Bahmni distribution, existing installations running the old OpenELIS fork will need a data migration path. This should be built as a **productized tool** (not a one-off script) since it will be needed by many installations.

### 6.1 Scope (To Be Defined Later)

**Data that will need to migrate:**
- Patients and demographics
- Samples, accessions, and analyses
- Test results (historical and in-progress)
- Test catalog (if mastering model changes — see Section 4)
- Health centers → organizations

**Data that does NOT migrate:**
- AtomFeed infrastructure tables (`failed_events`, `markers`, `event_records`, etc.)
- User sessions, login history

### 6.2 Key Considerations

- Both systems share the `clinlims` database/schema ancestry, but OE-Global-2 has evolved the schema significantly (dozens of Liquibase migration files from v2.0.x through v3.2.x)
- Migration tooling must be repeatable, validated, and tested against production data
- This work can be planned and executed independently from the current integration effort

---

## 7. Phases

| Phase | Scope | Duration | Outcome |
|---|---|---|---|
| **1. PoC** | Deploy OE-Global-2 standalone. Connect to a test OpenMRS instance via FHIR. Validate: can a lab order placed in Bahmni flow to OE-Global-2? Can a result flow back? Involve Angshuman Sarkar. | 3-4 weeks | Go/no-go on FHIR integration feasibility; identify gaps in Bahmni FHIR2 module |
| **2. Reference data** | Decide on mastering model (Section 4). Implement chosen approach. Configure Bahmni default test catalog. | 2-3 weeks | Test catalog available in OE-Global-2, synchronized with OpenMRS |
| **3. Full integration** | End-to-end lab workflow: order → sample → result → validation → published back to OpenMRS. Docker Compose integration. | 3-5 weeks | Working Bahmni + OE-Global-2 stack for current client |
| **4. Rollout** | Deploy to current client (fresh install, no data migration). | 1-2 weeks | Production deployment |
| **Future: Data migration tooling** | When OE-Global-2 is rolled into the standard Bahmni release, build productized migration tool for existing installations. | TBD | Reusable migration tool for existing Bahmni installations upgrading to OE-Global-2 |

**Total for current client (Phases 1-4):** 8-14 weeks
**Data migration tooling:** Planned separately when OE-Global-2 becomes part of standard Bahmni release

---

## 8. Open Questions

| # | Question | Needed For | Owner |
|---|---|---|---|
| 1 | What is the FHIR integration complexity on the OpenMRS/Bahmni side? Does the FHIR2 module already create Task resources for lab orders? | Phase 1 PoC scoping | Angshuman Sarkar |
| 2 | Which reference data sync approach? (Section 4) | Phase 2 | Team decision |
| 3 | What is the subscription/polling model for results? Does OpenMRS poll OE-Global-2's FHIR server, or does OE-Global-2 push? | Phase 1 PoC | Angshuman Sarkar |
| 4 | Is standalone patient sync needed, or is patient-on-demand sufficient? | Phase 1 PoC | SME |
| 5 | FHIR server topology — shared, separate, or poll-OpenMRS-directly? (Section 3.4) | Phase 1 PoC | Team decision |

---

*Supporting detail on Bahmni customizations, code inventory, and feature-level analysis is available in [archive/migration-analysis-openelis-global-v3.md](archive/migration-analysis-openelis-global-v3.md) and [archive/openelis-global-2-integration-approach.md](archive/openelis-global-2-integration-approach.md) for reference.*
