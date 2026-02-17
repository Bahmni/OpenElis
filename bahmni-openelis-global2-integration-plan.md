# Bahmni + OpenELIS-Global-2: Integration Plan

**Date:** 2026-02-17
**Status:** Draft
**Objective:** Replace Bahmni's OpenELIS fork with OpenELIS-Global-2 (OE-Global-2), integrated via FHIR.

---

## 1. Context

Bahmni currently ships a fork of OpenELIS (v3.1, circa 2013) integrated with OpenMRS via AtomFeed. OpenELIS-Global-2 is the actively maintained successor with native FHIR R4 support. We are adopting it as-is — no code porting, no forking.

**The work is integration:** making OE-Global-2 and Bahmni's OpenMRS exchange lab orders, results, and reference data via FHIR.

---

## 2. Four Questions to Solve

### Q1: How do I send a lab order to OE-Global-2?

**Short answer:** OE-Global-2 polls a FHIR endpoint for `Task` resources. It can poll OpenMRS's FHIR2 module directly — but it also needs a local FHIR store (HAPI) for its own resources.

**How it works in OE-Global-2:**

OE-Global-2 has a scheduled poller (`FhirApiWorkFlowServiceImpl`) that periodically queries a remote FHIR endpoint for new Tasks:

```
OpenMRS FHIR2 endpoint                    OE-Global-2
┌─────────────────────┐                   ┌──────────────────┐
│ Task (REQUESTED)    │◀── polls every ───│ FhirApiWorkFlow  │
│ + ServiceRequest    │    2 minutes      │ ServiceImpl      │
│ + Patient           │                   │                  │
└─────────────────────┘                   └──────────────────┘
```

Configuration (`application.properties`):
```properties
# OE-Global-2 polls THIS endpoint for lab order Tasks
org.openelisglobal.remote.source.uri=http://openmrs:8080/openmrs/ws/fhir2/R4

# How often to poll (default: 120 seconds)
org.openelisglobal.remote.poll.frequency=120000

# Practitioner ID that "owns" the tasks (filters which tasks to pick up)
org.openelisglobal.remote.source.identifier=<practitioner-id>
```

The poller searches for `Task` resources with status `REQUESTED`, fetches the linked `ServiceRequest` and `Patient`, and creates an electronic order in OE-Global-2.

**Can I skip the separate HAPI FHIR server?**

Not entirely. OE-Global-2 uses **two FHIR endpoints**:

| Endpoint | Purpose | Can it be OpenMRS? |
|---|---|---|
| **Remote source** (`remote.source.uri`) | Poll for incoming Tasks/orders | **Yes** — can point directly at OpenMRS FHIR2 |
| **Local FHIR store** (`fhirstore.uri`) | Store OE-Global-2's own FHIR resources (DiagnosticReport, Observation, Task status updates) | **No** — needs a dedicated HAPI FHIR server. OE-Global-2 writes transaction bundles here that OpenMRS's FHIR2 module may not support as a general-purpose FHIR store |

**Verdict:** You need the HAPI FHIR container, but it can be lightweight. OE-Global-2 reads from OpenMRS directly, writes to its own HAPI FHIR store.

**What Bahmni/OpenMRS needs to do:**

When a clinician places a lab order, OpenMRS must create:
1. A FHIR `ServiceRequest` with LOINC-coded test (see Q3)
2. A FHIR `Task` referencing the ServiceRequest + Patient, with status `REQUESTED`

The Bahmni FHIR2 module ([openmrs-module-fhir2Extension](https://github.com/Bahmni/openmrs-module-fhir2Extension)) needs to produce these resources. **This is the key OpenMRS-side work — needs Angshuman Sarkar's assessment.**

**Open:** Does the Bahmni FHIR2 module already create Task resources on lab order, or only ServiceRequest?

---

### Q2: How does the EMR know results are ready? How do I get results back?

**Short answer:** OE-Global-2 pushes FHIR `DiagnosticReport` + `Observation` to its local FHIR store when results are validated, and updates the Task status to `COMPLETED`. A data export service can then push these to OpenMRS.

**What happens when a lab tech validates results:**

1. Lab tech validates results in OE-Global-2's React UI
2. `FhirTransformService` creates:
   - `DiagnosticReport` (status = `FINAL`)
   - `Observation` resources for each result (status = `FINAL`)
   - Updates the `Task` status to `COMPLETED` with DiagnosticReport references in `Task.output`
3. All resources are pushed to OE-Global-2's local HAPI FHIR store as a transaction bundle

**Task status lifecycle** (maps to sample processing stages):

| Lab stage | Task status |
|---|---|
| Order received | `ACCEPTED` |
| Sample entered | `READY` |
| Testing started | `IN_PROGRESS` |
| Results finalized | `COMPLETED` (DiagnosticReport ref in output) |
| Rejected | `REJECTED` |

**How OpenMRS gets the results — two options:**

**Option A: Data Export Service (built-in)**

OE-Global-2 has a `DataExportService` that pushes FHIR resources from its local store to a remote FHIR endpoint:

```properties
# OE-Global-2 pushes results to THIS endpoint
org.openelisglobal.fhir.subscriber=http://openmrs:8080/openmrs/ws/fhir2/R4
org.openelisglobal.fhir.subscriber.resources=Task,DiagnosticReport,Observation
```

This pushes transaction bundles containing finalized DiagnosticReports and Observations to OpenMRS.

**Option B: Task status update back to remote**

OE-Global-2 can optionally update the Task status directly on the remote source (OpenMRS):

```properties
org.openelisglobal.remote.source.updateStatus=true
```

When enabled, OE-Global-2 updates the original Task on OpenMRS to `COMPLETED` with result references. OpenMRS can then resolve the DiagnosticReport references.

**Option C: OpenMRS polls OE-Global-2's FHIR store**

OpenMRS FHIR2 module polls OE-Global-2's HAPI FHIR endpoint for completed DiagnosticReports.

**Recommendation:** Option A (data export) or Option B (task status update) — both are push-based and already built into OE-Global-2. Needs PoC validation to confirm which works best with OpenMRS FHIR2 module.

**What Bahmni/OpenMRS needs to do:**

- Accept incoming FHIR DiagnosticReport + Observation resources (via FHIR2 module)
- Display lab results in the clinical UI from these FHIR resources
- **Open:** Can OpenMRS FHIR2 module accept pushed DiagnosticReports, or does it only serve them?

---

### Q3: How does OE-Global-2 know what test to run? LOINC only, or custom codes?

**Short answer:** OE-Global-2 matches incoming orders to tests using **LOINC codes only** in the FHIR workflow. Custom codes can exist internally but are not used for order matching.

**How order-to-test matching works:**

When OE-Global-2 receives a FHIR `ServiceRequest`, the `TaskInterpreter` extracts the test code:

```
ServiceRequest.code.coding[] → look for system = "http://loinc.org" → match LOINC code to Test or Panel
```

Specifically:
1. Loop through `ServiceRequest.code.coding` entries
2. Find the one with system `http://loinc.org`
3. Look up the LOINC code in OE-Global-2's test catalog (`testService.getTestsByLoincCode()`)
4. If no test found, try panel lookup (`panelService.getPanelByLoincCode()`)
5. If neither found → order rejected as `UNSUPPORTED_TESTS`

**There is no fallback** to custom codes, test names, or other coding systems in the FHIR path.

**Test entity structure:**

| Field | Max Length | Used for FHIR matching? | Required? |
|---|---|---|---|
| `loinc` | 240 chars | **Yes — the only field used** | Required for FHIR orders |
| `local_code` | 10 chars (unique) | No | Optional, internal use |
| `description` | 60 chars (unique) | No | Required, human-readable |
| `name` | varies | No | Localized display name |

**Can a CBC panel have different component tests?**

Yes. Panels are composed via `PanelItem` entities that link a panel to its component tests. You can define a CBC panel with whatever component tests you need. Both the panel and each component test need LOINC codes for FHIR matching.

**Implication for Bahmni:**

- Every test and panel in Bahmni/OpenMRS that will be ordered as a lab test **must have a LOINC code**
- The same LOINC code must be configured in both OpenMRS (for order creation) and OE-Global-2 (for order interpretation)
- If Bahmni currently uses custom/local codes for lab tests, LOINC codes need to be added

**Open:** Does the current Bahmni test catalog have LOINC codes, or only custom codes? If custom only, LOINC mapping is a prerequisite.

---

### Q4: How do I set up master data (centers, users, tests, panels, ranges)?

**Short answer:** OE-Global-2 has an admin UI, REST APIs, CSV bulk import on startup, and FHIR-based import for organizations and providers.

**Summary of setup methods:**

| Master Data | Admin UI | CSV Import (startup) | FHIR Import | REST API |
|---|---|---|---|---|
| **Tests** | TestAddController | `configuration/tests/*.csv` | No | TestAddRestController |
| **Panels** | PanelCreateController | via tests CSV | No | PanelCreateRestController |
| **Test Sections** | TestSectionCreateController | via tests CSV | No | REST API |
| **Sample Types** | SampleTypeCreateController | `configuration/sampleTypes/*.csv` | No | REST API |
| **Result Ranges** | resultLimitsMenu.jsp | No | No | REST API |
| **Organizations/Centers** | OrganizationController | No | **Yes** (FHIR Organization) | OrganizationRestController |
| **Users** | UnifiedSystemUserController | No | **Yes** (FHIR Practitioner) | UnifiedSystemUserRestController |
| **Roles** | via UI | `configuration/roles/*.csv` | No | REST API |
| **Dictionaries** | DictionaryController | `configuration/dictionaries/*.csv` | No | DictionaryRestController |

**CSV bulk import (recommended for initial setup):**

OE-Global-2 auto-loads CSV files on application startup from:
- Classpath: `src/main/resources/configuration/`
- Filesystem: `/var/lib/openelis-global/configuration/backend/` (Docker volume)

Test CSV format:
```
testName,testSection,sampleType,loinc,isActive,isOrderable,sortOrder,unitOfMeasure,englishName,frenchName
```

Test results CSV format:
```
testName,resultType,resultValue,sortOrder,isQuantifiable,isActive,isNormal,significantDigits,flags
```

Files are checksum-tracked — only processed when changed. This is the cleanest way to set up the initial test catalog.

**FHIR-based import for organizations and providers:**

```properties
# Import organizations/facilities from a FHIR server
org.openelisglobal.facilitylist.fhirstore=http://openmrs:8080/openmrs/ws/fhir2/R4

# Import practitioners from a FHIR server
org.openelisglobal.providerlist.fhirstore=http://openmrs:8080/openmrs/ws/fhir2/R4
```

Both run on a schedule and can import from OpenMRS's FHIR2 endpoint directly.

**Also supports:** OpenConceptLab (OCL) import for test catalogs:
```properties
org.openelisglobal.ocl.import.autocreate=true
```

**Recommended setup approach for Bahmni:**

1. **Tests, panels, sample types, dictionaries** → CSV files in a Docker volume. Create a "Bahmni default" configuration set.
2. **Result ranges** → Admin UI or REST API (no CSV import for ranges).
3. **Organizations/centers** → FHIR import from OpenMRS (automatic).
4. **Users/providers** → FHIR import from OpenMRS (automatic) + local user creation for lab-specific accounts.

---

## 3. Additional Integration Concerns

Beyond the SME's four questions, these items need attention:

### 3.1 Reference Data Sync (Tests/Panels from OpenMRS)

Tests and panels are currently mastered in OpenMRS and synced to OpenELIS. OE-Global-2 manages its test catalog locally. Three options:

| Option | Description | Effort |
|---|---|---|
| **A. OE-Global-2 owns the test catalog** | Configure tests in OE-Global-2 (via CSV or admin UI). Sync to OpenMRS for order entry dropdowns. | Low — but changes current workflow |
| **B. Shared CSV config** | Maintain a single CSV file set that configures both systems | Low — operational process |
| **C. FHIR-based sync from OpenMRS** | Build sync from OpenMRS to OE-Global-2 | High — FHIR test catalog representation is not well-established |

**Recommendation:** Option A or B. The LIS should own its test catalog. A shared CSV config set (checked into version control as the "Bahmni default") keeps both systems aligned without building custom sync.

### 3.2 LOINC Code Requirement

OE-Global-2 requires LOINC codes for FHIR order matching. If Bahmni's current test catalog uses only custom codes, **LOINC mapping is a prerequisite** before integration can work. This is potentially significant work depending on the size of the test catalog.

**Action needed:** Audit the current Bahmni test catalog for LOINC coverage.

### 3.3 Patient Sync

Patient data flows as part of the FHIR Task context — when a lab order arrives, the Patient resource is included. OE-Global-2 creates/updates the patient in its registry automatically.

**Open:** Is standalone patient sync needed (patient registered but no lab order yet), or is patient-on-demand sufficient?

### 3.4 Authentication Between Systems

OE-Global-2's FHIR client supports basic auth:
```properties
org.openelisglobal.fhirstore.username=<username>
org.openelisglobal.fhirstore.password=<password>
```

OpenMRS FHIR2 endpoint authentication needs to be compatible. The facility list import also supports token-based auth.

### 3.5 Deployment

OE-Global-2 adds 5 containers to the Bahmni Docker Compose stack (webapp, database, HAPI FHIR, React frontend, nginx proxy), replacing the current 2 (openelis, openelis-db). Net +3 containers.

```
Current:   openmrs + openmrs-db + openelis + openelis-db + odoo + odoo-db
Target:    openmrs + openmrs-db + oe-global-webapp + oe-global-db + hapi-fhir + oe-global-frontend + oe-global-proxy + odoo + odoo-db
```

---

## 4. Integration Architecture

```
┌─────────────────────────┐         ┌──────────────────────────────────┐
│      OpenMRS (Bahmni)   │         │      OpenELIS-Global-2           │
│                         │  polls  │                                  │
│  FHIR2 Module ──────────│────────▶│  FhirApiWorkFlowServiceImpl      │
│  (Task + ServiceRequest)│ Tasks   │  (creates ElectronicOrder)       │
│                         │         │                                  │
│                         │  push   │  FhirTransformService            │
│  FHIR2 Module ◀─────────│────────│  (DiagnosticReport + Observation)│
│  (DiagnosticReport)     │ results │                                  │
│                         │         │         ┌──────────┐             │
│  FHIR2 Module ──────────│────────▶│         │ HAPI FHIR│             │
│  (Organization,         │  FHIR   │         │ (local)  │             │
│   Practitioner)         │  import │         └──────────┘             │
│                         │         │                                  │
│                         │         │  React Frontend (lab users)      │
└─────────────────────────┘         └──────────────────────────────────┘
         │
         │ (unchanged)
         ▼
┌─────────────────────────┐
│      Odoo (billing)     │  ← no direct connection to OE-Global-2
└─────────────────────────┘
```

---

## 5. Phases

| Phase | Scope | Duration | Key Question Answered |
|---|---|---|---|
| **1. PoC** | Deploy OE-Global-2 standalone. Point it at an OpenMRS FHIR2 endpoint. Place a lab order in Bahmni, see if OE-Global-2 picks it up. Validate a result, see if it reaches OpenMRS. | 3-4 weeks | Q1 + Q2 validated end-to-end |
| **2. Test catalog** | Audit Bahmni test catalog for LOINC codes. Create CSV config files for OE-Global-2. Validate order matching. | 2-3 weeks | Q3 resolved |
| **3. Master data + deployment** | Configure organizations, users, result ranges. Integrate into Bahmni Docker Compose stack. | 2-3 weeks | Q4 resolved |
| **4. End-to-end testing** | Full lab workflow with real test scenarios. User acceptance testing with lab technicians. | 2-3 weeks | Production readiness |
| **5. Go-live** | Deploy to current client (fresh install). | 1 week | |

**Total: 10-14 weeks**

**Future (when OE-Global-2 becomes standard Bahmni release):**
- Data migration tooling for existing installations (productized, reusable)

---

## 6. Open Questions

| # | Question | Blocks | Owner |
|---|---|---|---|
| 1 | Does the Bahmni FHIR2 module create `Task` resources on lab order, or only `ServiceRequest`? | Phase 1 | Angshuman Sarkar |
| 2 | Can OpenMRS FHIR2 module accept pushed DiagnosticReports (write), or only serve them (read)? | Phase 1 | Angshuman Sarkar |
| 3 | Does the current Bahmni test catalog have LOINC codes? How many tests need LOINC mapping? | Phase 2 | SME |
| 4 | Where should the test catalog be mastered — OpenMRS or OE-Global-2? | Phase 2 | Team decision |
| 5 | Is standalone patient sync needed, or is patient-on-demand (synced with first lab order) sufficient? | Phase 1 | SME |

---

*Archived analysis documents with detailed code inventory available in [archive/](archive/) for reference.*
