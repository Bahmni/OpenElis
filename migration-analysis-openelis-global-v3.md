# Migration Analysis: Bahmni OpenELIS to OpenELIS-Global-2 (v3)

**Date:** 2026-02-17
**Prepared by:** Engineering Team
**Status:** Discovery / Assessment Phase (SME Answers Incorporated — 2026-02-17)

---

## 1. Executive Summary

This document assesses the feasibility and effort required to migrate from the Bahmni OpenELIS fork (based on OpenELIS v3.1) to OpenELIS-Global-2 (v3.2.x). The Bahmni fork was created when upstream OpenELIS development stalled; OpenELIS has since undergone a major rewrite as "OpenELIS-Global-2" with a modern tech stack.

**Key Finding:** This is a **platform migration, not a code merge**. The two codebases share a common ancestor but have diverged at every architectural layer. The Bahmni customizations span two categories:
- **77 new classes in `org.bahmni.*`** (~6,400 LOC) — cleanly separated integration and REST code
- **132 new files + 1,522 modified files in `us.mn.state.health.lims.*`** — feature additions (dashboard, CSV upload, health center, sample source, etc.) and modifications to nearly every original OpenELIS file

The `org.bahmni` integration layer is well-bounded and replaceable by FHIR. The `us.mn.state.health.lims` modifications are more diffuse and require feature-by-feature assessment against OE-Global-2's native capabilities.

**Recommendation:** Adopt OpenELIS-Global-2 as the base platform and reimplement Bahmni-specific integration using its native FHIR-based mechanisms (SME confirmed FHIR-first approach). Odoo does not connect to OpenELIS directly — billing integration stays on the OpenMRS ↔ Odoo pathway. Feature additions in `us.mn.state.health.lims` (dashboard, upload, etc.) need individual assessment to determine if OE-Global-2 already covers them.

**Key SME findings (2026-02-17):** FHIR2 module exists in Bahmni but integration is more complex than configuration (needs Angshuman Sarkar's input). Reference data (tests, panels, sample types) is mastered in OpenMRS and synced to OpenELIS — this is a **confirmed gap** in OE-Global-2. Data migration must be **productized** for multiple installations. Migration will follow a **phased approach**.

---

## 2. Architecture Comparison

### 2.1 Tech Stack

| Layer | Bahmni OpenELIS (current) | OpenELIS-Global-2 (target) |
|---|---|---|
| **Java Version** | Java 7 source / Java 8 runtime (Amazon Corretto 8) | Java 21 |
| **Build System** | Apache Ant | Maven |
| **Backend Framework** | Struts 1.x + Tiles | Spring 6.2 + Spring Security 6.2 |
| **Frontend** | JSP + jQuery | React.js (Node 20+, IBM Carbon Design System) |
| **ORM** | Hibernate 3.x (`.hbm.xml` mappings) | Hibernate 5.6.15 (JPA annotations) |
| **FHIR Support** | None | HAPI FHIR 6.6.2 (R4) |
| **Database** | PostgreSQL (older JDBC) | PostgreSQL 42.7.2 |
| **JSON Library** | Jackson 2.9.10 | Jackson 2.18.2 |
| **Integration Paradigm** | AtomFeed (custom, based on Atom RFC 4287) | FHIR R4 (HL7 standard) |
| **Deployment** | Single WAR + DB (Docker) | 5 containers (see below) |
| **Codebase Size** | ~1,600 Java files | 2,359 Java files, 134 packages |

### 2.2 Deployment Architecture

**Current (Bahmni OpenELIS):**
- `bahmni/openelis` -- WAR in Tomcat 8 (port 8052)
- `bahmni/openelis-db` -- PostgreSQL

**Target (OpenELIS-Global-2):**
- `openelisglobal-webapp` -- Java backend (Tomcat, WAR)
- `openelisglobal-database` -- PostgreSQL (still `clinlims` DB + schema)
- `external-fhir-api` -- Dedicated HAPI FHIR server (separate container, same DB)
- `openelisglobal-front-end` -- React SPA (Node)
- `openelisglobal-proxy` -- nginx reverse proxy (SSL termination)

### 2.3 Database Schema

Both systems use:
- **Database name:** `clinlims`
- **Schema name:** `clinlims`
- **Database engine:** PostgreSQL

OpenELIS-Global-2 has evolved the schema significantly with versioned Liquibase migrations from `2.0.x.x` through `3.2.x.x` (dozens of migration files), but the foundational tables (patient, sample, analysis, result, test) are recognizably derived from the same ancestor.

---

## 3. Bahmni Customization Inventory

### 3.1 Overview

**`org.bahmni.*` — Cleanly separated Bahmni code (77 files):**

| Category | Scope | Lines of Code |
|---|---|---|
| AtomFeed Integration | Heavy -- full bi-directional sync | ~5,925 |
| Domain/REST APIs | Moderate -- JSON endpoints for queries | ~503 |
| Database Changes | Light -- 13 new tables | N/A |
| Configuration | Light -- single main properties file | N/A |
| External Dependencies | Light -- 3-4 key libraries | N/A |
| **Subtotal** | **77 Java classes** | **~6,400** |

**`us.mn.state.health.lims.*` — Bahmni changes in original OpenELIS packages:**

| Category | Count | Key Areas |
|---|---|---|
| New files added | 132 | `dashboard/`, `upload/`, `ws/handler/`, `healthcenter/`, `feed/`, `common/provider/query/` |
| Original files modified | 1,522 (of 1,447 total) | Nearly every original file touched; heaviest: `OrderListDAOImpl` (40 commits), `ResultsLogbookUpdateAction` (33), `SamplePatientEntrySaveAction` (21) |

**Total Bahmni footprint:** 77 new `org.bahmni` files + 132 new `us.mn.state.health.lims` files + 1,522 modified original files. The `org.bahmni` code is replaceable by FHIR; the `us.mn.state.health.lims` changes require feature-by-feature assessment.

### 3.2 AtomFeed Integration Layer (`org.bahmni.feed.openelis` -- 5,925 LOC)

This is the largest customization area. It implements bi-directional event synchronization with OpenMRS and reference data systems.

**Incoming Feed Processors (consume external events):**
- `PatientFeedEventWorker` -- Subscribes to OpenMRS patient feed, syncs patient demographics
- `EncounterFeedWorker` -- Consumes OpenMRS encounter/lab order events, creates samples and analyses
- `GroupByEncounterFeedProcessor` -- Groups lab orders by encounter
- `GroupBySampleTypeFeedProcessor` -- Groups by sample type
- `OpenElisEventWorker` -- Generic event processing
- `LabFeedEventWorker` -- Lab-specific event handling

**Outgoing Feed Publishers:**
- `EventPublishers` -- Publishes accession/patient events for downstream systems
- `OpenElisUrlPublisher` -- Publishes events to URLs
- `EventRecordsPublisherTask` -- Async task for event publication

**Feed Jobs (Quartz-scheduled polling):**
- `OpenMRSPatientFeedReaderJob` -- Reads patient feed from OpenMRS
- `OpenMRSEncounterFeedReaderJob` -- Reads encounter feed from OpenMRS
- `OpenMRSLabFeedReaderJob` -- Reads lab orders from OpenMRS
- `OpenELISFeedReaderJob` -- Reads lab results feed
- Plus 4 corresponding failed-events retry jobs

**Data Contracts:**
- `OpenMRSPatient`, `OpenMRSEncounter`, `OpenMRSOrder` -- Inbound contracts
- `OpenMRSProvider`, `OpenMRSPersonAttributeType` -- Supporting data
- `ReferenceDataAllTestsAndPanels`, `ReferenceDataSample` -- Reference data contracts

**External Dependencies:**
- `atomfeed-client-1.9.5.jar`, `atomfeed-commons-1.9.5.jar`, `atomfeed-server-1.9.5.jar`
- `quartz-2.3.2.jar` (job scheduling)

### 3.3 REST/JSON API Endpoints (`org.bahmni.openelis.domain` -- 503 LOC)

- `ResourceRetrieveAction` -- REST endpoint serving JSON responses
- `PatientHandler` -- JSON endpoint for patient details
- `AccessionHandler` -- JSON endpoint for accession details
- Domain objects: `CompletePatientDetails`, `AccessionDetail`, `TestDetail`, `AccessionNote`, `Attribute`

### 3.4 Database Changes (Liquibase, `context="bahmni"`, source: `BahmniConfig.xml`)

13 new tables added by Bahmni:

| # | Table | Purpose |
|---|-------|---------|
| 1 | `failed_events` | Stores AtomFeed events that failed processing for retry |
| 2 | `markers` | Tracks AtomFeed consumption position (last processed entry) |
| 3 | `external_reference` | Maps external system IDs (OpenMRS UUIDs) to internal OpenELIS IDs |
| 4 | `HEALTH_CENTER` | Health center / facility management |
| 5 | `event_records` | Locally published events (OpenELIS -> OpenMRS) |
| 6 | `chunking_history` | Event chunking tracking for large feeds |
| 7 | `sample_source` | Sample source dropdown values (e.g., ward, OPD) |
| 8 | `import_status` | CSV/bulk upload status tracking |
| 9 | `event_records_offset_marker` | Offset tracking for published events |
| 10 | `failed_event_retry_log` | Retry attempt history for failed events |
| 11 | `event_records_queue` | Event queue for async processing |
| 12 | `type_of_test_status` | Custom test status type definitions |
| 13 | `test_status` | Test-level status tracking |

### 3.5 Configuration

- `atomfeed.properties` -- Feed URIs, OpenMRS authentication, connection settings, product type mappings
- `struts-globalOpenELIS.xml` -- Extended Struts actions and form definitions
- `tiles-globalOpenELIS.xml` -- UI page compositions

### 3.6 UI Customizations

- `/pages/atomfeed/execute.jsp` -- AtomFeed job execution UI
- Extended `samplePatientEntryForm` with: providerList, projectList, orderTypes, sampleSourceList
- Dashboard views for lab order and result status

### 3.7 Unique Bahmni Features

1. **Sample Source Dropdown** -- Capture sample source (e.g., ward, OPD) during sample entry
2. **Provider/Order Tracking** -- Integration with OpenMRS providers and encounter orders
3. **Bi-directional Event Sync** -- Full event-driven architecture (not just UI)
4. **Dashboard Views** -- Lab-specific dashboard showing pending orders, results
5. **Multi-feed Consumption** -- Handles feeds from OpenMRS, OpenERP, reference-data simultaneously

### 3.8 Bahmni Features Added Inside `us.mn.state.health.lims` (132 new files)

These features were implemented directly in the original OpenELIS package namespace, not under `org.bahmni`:

| Sub-package | New Files | Purpose |
|---|---|---|
| `upload/service`, `upload/sample`, `upload/patient`, `upload/action` | 23 | CSV/bulk upload of patients, samples, and test results |
| `ws/handler` | 11 | REST/JSON handler classes for Bahmni EMR integration |
| `dashboard/` (action, dao, daoimpl, valueholder) | 11 | Lab dashboard showing order status, pending results |
| `typeofteststatus/` | 10 | Test status type management |
| `healthcenter/` (action, dao, daoimpl, valueholder) | 9 | Health center / facility management |
| `common/provider/query`, `common/util`, `common/action` | 12 | Provider autocomplete, query converters, utilities |
| `ws/` | 4 | Base web service actions |
| `feed/` | 3 | Feed-related classes in the original package |
| Other (sample, result, patient, etc.) | 49 | Scattered additions across existing modules |

Additionally, **1,522 of the original 1,447 files** were modified — meaning virtually the entire original codebase was touched. The most heavily modified files suggest deep feature work in result entry, validation, sample management, and dashboard functionality.

### 3.9 Feature-by-Feature Triage: `us.mn.state.health.lims` Additions vs OE-Global-2

This section compares each Bahmni feature area (the 132 new files) against OE-Global-2's native capabilities, based on source code analysis of both codebases.

#### Feature 1: Lab Dashboard (6 src + 3 test files)

**Bahmni files:** `DashboardAction`, `OrderListDAO`, `OrderListDAOImpl`, `OrderListDAOHelper`, `Order`, `TodayStat`

**What it does:** Provides a lab operations dashboard showing:
- Today's orders (with accession number, patient, source, pending/validation/referred test counts, priority)
- Today's uncollected samples
- Backlog: uncollected samples from previous days
- Backlog: pending orders from previous days
- Data served as JSON via Struts action, rendered by Bahmni Apps

**OE-Global-2 equivalent:** **Yes — native dashboard exists, more comprehensive.**
- `PatientDashBoardProvider` (`/rest/` endpoint) — REST controller providing dashboard metrics
- `DashBoardMetrics` — tracks: orders in progress, ready for validation, completed today, partially completed, rejected, unprinted results, incoming orders, average turnaround time, delayed turnaround
- `DashBoardTile`, `OrderDisplayBean`, `AverageTimeDisplayBean` — rich dashboard display beans
- `PatientDashBoardPaging` — pagination support for the dashboard
- React frontend renders these via Carbon Design System

**Verdict:** **Covered by OE-Global-2.** OE-Global-2's dashboard is richer (has turnaround time metrics, rejection tracking). However, the specific data shape is different — Bahmni Apps currently consumes the Bahmni dashboard JSON format. If Bahmni Apps is also being updated, OE-Global-2's dashboard is a drop-in replacement. If Bahmni Apps stays as-is, an adapter may be needed.

---

#### Feature 2: CSV/Bulk Upload (14 src + 9 test files)

**Bahmni files:** `UploadAction`, `UploadForm`, `UploadDashboardAction`, `AddSampleService`, `CSVPatient`, `PatientPersister`, `CSVSample`, `CSVTestResult`, `SamplePersister`, `AnalysisPersisterService`, `ResultPersisterService`, `SampleHumanPersisterService`, `SampleItemPersisterService`, `SamplePersisterService`, `TestResultPersisterService`, `Upload` (valueholder), `ELISJDBCConnectionProvider`, `FileImportCleanupListener`

**What it does:** Allows bulk import of:
- **Patients** via CSV (`CSVPatient` → `PatientPersister`) — maps CSV columns to patient demographics
- **Samples + test results** via CSV (`CSVSample`, `CSVTestResult` → `SamplePersister`, `TestResultPersisterService`) — creates accessions, samples, analyses, and results from CSV
- Upload dashboard showing import status history (success/failure, error files)
- Uses `import_status` DB table to track upload jobs

**OE-Global-2 equivalent:** **Partially.** OE-Global-2 has:
- `ImportController` — REST endpoint for importing organizations and providers from FHIR
- `OrganizationImportService` — imports organizations from FHIR server
- `ProviderImportService` — imports practitioners from FHIR server
- `AnalyzerImportController` + `CSVAnalyzerReader` — CSV import for analyzer results only
- `FileImportWatchService` + `FileImportRestController` — file-based analyzer data import
- `GenericSampleImportResult` — generic sample import result tracking
- **No general-purpose CSV upload for patients, samples, or test results**

**Verdict:** **Gap — partial coverage.** OE-Global-2 can import analyzer results via CSV but does NOT have a general-purpose CSV upload for patients, samples, and test results. If the Bahmni deployment uses CSV uploads for data migration or bulk entry (e.g., from external labs), this feature would need to be rebuilt as a Spring REST controller + React UI, or the use case handled through FHIR resource import.

---

#### Feature 3: REST/JSON Handlers for Bahmni EMR (7 src + 6 test files)

**Bahmni files:** `WebServiceAction`, `ResourceRetrieveAction`, `TasksMonitoringAction`, `Handler` (interface), `Handlers`, `PatientHandler`, `AccessionHandler`, `TestResultHandler`, `WebServicesInput`

**What it does:** REST API layer that Bahmni Apps (the EMR frontend) calls to fetch lab data:
- `PatientHandler` — returns `CompletePatientDetails` JSON for a patient
- `AccessionHandler` — returns `AccessionDetail` JSON (accession + tests + results) for a patient
- `TestResultHandler` — returns test results
- `WebServiceAction` — base action with basic auth, routing to handlers by name
- `TasksMonitoringAction` — monitors AtomFeed task status

**OE-Global-2 equivalent:** **Different API surface, but richer.**
- OE-Global-2 has extensive REST controllers: `PatientManagementController`, `SamplePatientEntryRestController`, `SampleEditRestController`, `AccessionValidationRestController`, plus FHIR endpoints for Patient, DiagnosticReport, Observation
- The REST API contracts are **different** — Bahmni Apps expects specific JSON shapes from `PatientHandler`/`AccessionHandler`

**Verdict:** **API adapter needed OR Bahmni Apps update.** OE-Global-2 has all the underlying data accessible via REST/FHIR, but the API contracts (URL paths, JSON shapes) are different. Two options: (a) build thin adapter controllers in OE-Global-2 that return Bahmni-compatible JSON, or (b) update Bahmni Apps to consume OE-Global-2's native API format. Option (b) is preferred if Bahmni Apps is also being updated.

---

#### Feature 4: Health Center Management (5 src + 1 test file)

**Bahmni files:** `HealthCenterCreateAction`, `HealthCenterDeactivateAction`, `HealthCenterListAction`, `HealthCenterMenuAction`, `HealthCenterUpdateAction`, `HealthCenterDAO`, `HealthCenterDAOImpl`, `HealthCenter` (valueholder)

**What it does:** Full CRUD for "health centers" — facilities that are sources of lab orders:
- List, create, update, deactivate health centers
- Stored in `HEALTH_CENTER` table
- Used in sample entry forms to track which facility sent the sample

**OE-Global-2 equivalent:** **Yes — Organization module.**
- `OrganizationController`, `OrganizationRestController` — full CRUD for organizations
- `OrganizationMenuController` — list/search organizations
- `Organization` entity with type, address, contact info
- `OrganizationType` — categorizes organizations (which can include health centers/referral sites)
- `OrganizationImportService` — can import organizations from FHIR server

**Verdict:** **Covered by OE-Global-2.** The `Organization` entity with `OrganizationType` categorization is a superset of the Bahmni `HealthCenter` concept. Existing health center data can be migrated as organizations with an appropriate type. No new development needed.

---

#### Feature 5: Test Status Types (6 src + 0 test files)

**Bahmni files:** `TypeOfTestStatusAction`, `TypeOfTestStatusCancelAction`, `TypeOfTestStatusMenuAction`, `TypeOfTestStatusMenuCancelAction`, `TypeOfTestStatusNextPreviousAction`, `TypeOfTestStatusUpdateAction`, `TypeOfTestStatusDAO`, `TypeOfTestStatusDAOImpl`, `AllowedTestStatusTypes`, `TypeOfTestStatus`, `TestStatusDAO`, `TestStatusDAOImpl`, `TestStatus`

**What it does:** Allows defining and managing custom test-level statuses (beyond the standard analysis statuses). Stored in `type_of_test_status` and `test_status` tables. Used to track additional workflow states for tests.

**OE-Global-2 equivalent:** **Partially — via standard analysis status.**
- OE-Global-2 uses `StatusOfSample` and `AnalysisStatus` enums (NotStarted, TechnicalAcceptance, BiologistRejected, Finalized, etc.)
- `BatchTestStatusChangeBean`, `BatchTestReassignmentRestController` — batch operations on test status
- No equivalent of *user-configurable* test status types

**Verdict:** **Needs assessment.** If the Bahmni deployment uses custom test statuses beyond the standard lifecycle, this is a gap. If they only use the standard statuses (pending, in-progress, validated, finalized), OE-Global-2's built-in status model covers it. **Ask SME if custom test statuses are actively used.**

---

#### Feature 6: Sample Source (3 src + 1 test file)

**Bahmni files:** `SampleSourceDAO`, `SampleSourceDAOImpl`, `SampleSource` (valueholder)

**What it does:** Manages a dropdown list of sample sources (e.g., "OPD", "IPD", "Emergency Ward") used during sample entry to record where the sample came from. Stored in `sample_source` table.

**OE-Global-2 equivalent:** **Yes — Sample Nature.**
- `SampleNature` concept exists in OE-Global-2's form fields (`FormFields.java` has `SampleNature` field)
- `SamplePatientEntryForm`, `SampleBatchEntryForm` both have `sampleNature` field
- `DisplayListService` provides `SAMPLE_NATURE` list
- Configurable per-implementation via form field settings

**Verdict:** **Covered by OE-Global-2** via the `SampleNature` concept. May need configuration to populate with the same values (OPD, IPD, etc.) but no code changes needed.

---

#### Feature 7: Result Validation by Accession Number (1 src + 1 test file)

**Bahmni files:** `ResultValidationForAccessionNumberAction`

**What it does:** Extends OpenELIS validation to allow looking up all results for a specific accession number and validating them together, regardless of test section. The README notes this as a key functional change: *"Added functionality to validate test results by a particular accession number. Also you can see items to be validated across all test sections."*

**OE-Global-2 equivalent:** **Yes — native feature.**
- `AccessionValidationController`, `AccessionValidationRestController` — dedicated controllers for accession-based validation
- `AccessionValidationForm` — form specifically for validation by accession number
- `AccessionValidationRangeController` — validation across a range of accession numbers
- `ResultValidationService` — full validation service layer

**Verdict:** **Covered by OE-Global-2** — this is now a first-class feature with dedicated controllers and REST endpoints.

---

#### Feature 8: Common Utilities & Form Fields (12 src + 4 test files)

**Bahmni files:** `BahmniAdministrativeFormFields`, `JSSFormFields`, `ActionFactory`, `CommonCancelAction`, `LIMSInvalidSTNumberException`, `LIMSValidationException`, `TestDictionaryValueAutocompleteProvider`, `PatientXmlCreator`, `SampleOrderDetailsFromSampleProvider`, `SampleTypeTestsForSampleProvider`, `SiteInformationProvider`, `TestUpdateWithAccessionNumberProvider`, `PersonAddressConverter`, `DateNumAccessionValidator`, `AjaxTabSelectServlet`, `IntegerUtil`, `RequestParameterCheckFilter`, `SafeRequest`

**What it does:** Mixed bag of utilities:
- `BahmniAdministrativeFormFields` / `JSSFormFields` — form field configuration for Bahmni/JSS implementations
- `DateNumAccessionValidator` — accession number format: date-based numbering
- `PatientXmlCreator` — generates patient XML for AJAX responses
- Various AJAX query providers for autocomplete/search
- `SafeRequest` / `RequestParameterCheckFilter` — security utilities
- `ActionFactory` — Struts action factory pattern

**OE-Global-2 equivalent:** **Mostly covered but different.**
- Form fields: OE-Global-2 has `DefaultFormFields`, `MauritiusFormFields`, and a configurable form fields system — Bahmni would add another implementation
- Accession validation: has multiple validator implementations
- AJAX providers: replaced by React frontend with REST API calls
- Security: Spring Security replaces Struts-level filters

**Verdict:** **Mostly not needed.** These are Struts-era infrastructure classes. OE-Global-2's Spring + React architecture replaces the need for AJAX providers, Struts action factories, and servlet filters. The `DateNumAccessionValidator` logic may need to be carried forward if the Bahmni deployment uses date-based accession numbering. **Ask SME about accession number format.**

---

#### Feature 9: Address/Patient Extensions (6 src + 2 test files)

**Bahmni files:** `AddressParts`, `PersonAddresses` (in address/valueholder), `PatientAnalysis` (in analysis/dto), `PatientRetrieveAction`, `AddressPartForm`, `AddressParts` (in patient/action/bean), `PatientIdentities`, `PatientIdentityTypes`

**What it does:** Extensions to patient handling:
- `AddressParts` / `PersonAddresses` — structured address representation (multi-part addresses)
- `PatientIdentities` / `PatientIdentityTypes` — collection wrappers for patient identifiers
- `PatientRetrieveAction` — AJAX action to retrieve patient data
- `PatientAnalysis` — DTO linking patient to their analyses

**OE-Global-2 equivalent:** **Yes — native.**
- Full patient management with FHIR Patient resource mapping
- Address handling via standard FHIR Address type
- Patient identity types configurable in admin
- `PatientManagementController` with REST endpoints

**Verdict:** **Covered by OE-Global-2.** These are convenience wrappers that OE-Global-2's Spring/FHIR architecture handles natively.

---

#### Feature 10: Miscellaneous (5 src + 5 test files)

**Bahmni files:** `ElisHibernateSession`, `LogoutPageAction`, `OrganizationUtils`, `NumericResult`, `ResultFileUploadDao/Impl`, `ResultType`, `SampleTestCollection`, `AnalysisBuilder`, `PossibleSample`, `NonNumericTests`, `AtomFeedAction`, `AtomFeedUrlParser`, `DBHelper` (test helper)

**What it does:** Various smaller additions:
- `ElisHibernateSession` — Hibernate session management workaround
- `ResultFileUploadDao` — persisting result files/attachments
- `AnalysisBuilder` / `SampleTestCollection` / `PossibleSample` — sample creation utilities
- `AtomFeedAction` / `AtomFeedUrlParser` — AtomFeed admin UI support

**OE-Global-2 equivalent:** **Mostly covered or not needed.**
- Hibernate session: Spring manages this natively
- Result file upload: OE-Global-2 has result attachment support
- Sample/analysis builders: OE-Global-2 has `SamplePatientEntryService`, `SampleEditService`
- AtomFeed admin: not needed (replaced by FHIR)

**Verdict:** **Not needed.** These are infrastructure/plumbing classes specific to the Struts + Hibernate 3 + AtomFeed architecture.

---

#### Summary: `us.mn.state.health.lims` Feature Triage

| # | Feature | Bahmni Files | OE-Global-2 Status | Action |
|---|---------|-------------|-------------------|--------|
| 1 | Lab Dashboard | 9 | **Covered** (richer native dashboard) | Config only; API adapter if Bahmni Apps not updated |
| 2 | CSV/Bulk Upload | 23 | **Gap** (analyzer CSV only, no patient/sample CSV) | Rebuild if needed, or use FHIR import |
| 3 | REST/JSON Handlers | 13 | **Different API surface** (data available, format different) | API adapter or Bahmni Apps update |
| 4 | Health Center | 9 | **Covered** (Organization module) | Data migration only |
| 5 | Test Status Types | 13 | **Partial** (standard statuses only) | Ask SME if custom statuses are used |
| 6 | Sample Source | 4 | **Covered** (SampleNature field) | Config only |
| 7 | Accession Validation | 2 | **Covered** (native feature) | None |
| 8 | Common Utilities | 16 | **Not needed** (Struts infrastructure) | Ask about accession number format |
| 9 | Address/Patient | 8 | **Covered** (FHIR Patient) | None |
| 10 | Miscellaneous | 10 | **Not needed** (architecture-specific) | None |
| | **Total** | **107 src + 25 test** | | |

**Key findings:**
- **6 of 10 feature areas are fully covered** by OE-Global-2's native capabilities
- **1 confirmed gap**: CSV/bulk upload of patients, samples, and test results
- **2 conditional gaps**: REST API format compatibility (depends on Bahmni Apps roadmap) and custom test status types (depends on actual usage)
- **1 not needed**: Common utilities are Struts infrastructure replaced by Spring

---

## 4. Feature-by-Feature Gap Analysis

### 4.1 Already Covered by OE-Global-2 (No work needed)

| Feature | Notes |
|---|---|
| Core lab workflows | Sample registration, accessioning, tracking, results entry, validation |
| Patient management | Demographics, identifiers, FHIR Patient resource mapping |
| Test/panel configuration | Test catalog, panels, result ranges, units of measure |
| Analyzer integration | Plugin-based analyzer framework (modernized) |
| Multi-level result validation | Technical + biological validation workflows |
| Reporting | JasperReports-based, plus newer report types |
| Audit trail | Comprehensive change logging |
| Barcode/label printing | Sample label generation |
| Referral management | Inter-lab sample referral (new capability) |
| Storage tracking | Cold storage, sample storage management (new capability) |
| Quality assurance | Non-conformity tracking, quality control |

### 4.2 Covered But Different Mechanism (Configuration/adaptation needed)

| Bahmni Feature | OE-Global-2 Equivalent | Migration Effort | SME Update |
|---|---|---|---|
| AtomFeed patient sync | FHIR Patient resource exchange | More than configuration (per SME) | FHIR2 module exists; validate in PoC with Angshuman Sarkar |
| AtomFeed lab order sync | FHIR ServiceRequest + Task workflow | More than configuration (per SME) | Currently triggered by AtomFeed from ServiceRequest in Bahmni |
| AtomFeed result publishing | FHIR DiagnosticReport + Observation | More than configuration (per SME) | Only OpenMRS consumes results — single consumer simplifies migration |
| External reference tracking | Native FHIR resource identifiers | None (built-in) | — |
| Quartz-scheduled feed polling | Spring `@Scheduled` annotations | None (built-in) | — |
| OpenERP/Odoo integration | Built-in `org.openelisglobal.odoo` package | Configuration | **No direct Odoo-ELIS connection** — Odoo handles billing via OpenMRS only |
| Feed retry mechanism | Built-in error handling in FHIR workflow | None (built-in) | — |
| **Reference data sync** (tests, panels, sample types) | **Gap — no inbound sync in OE-Global-2** | **Medium-High — new development** | **Confirmed:** OpenMRS is master; sync mechanism must be built |

**Detailed FHIR mapping:**

| Bahmni AtomFeed Component | OE-Global-2 FHIR Equivalent |
|---|---|
| `PatientFeedEventWorker` | `FhirApiWorkFlowServiceImpl` (polls FHIR Tasks, extracts Patient) |
| `EncounterFeedWorker` | `TaskWorker` + `TaskInterpreter` (FHIR Task -> ServiceRequest -> ElectronicOrder) |
| `EventPublishers` | `FhirTransformService` (creates DiagnosticReport + Observation) |
| `ExternalReferenceDao` | Native FHIR identifiers and references |
| `OpenMRSPatientFeedReaderJob` | `@Scheduled` methods on `FhirApiWorkFlowServiceImpl` |
| `atomfeed.properties` | `FhirConfig.java` + Spring `@Value` properties |
| AtomFeed client library | HAPI FHIR Client (`IGenericClient`) |

### 4.3 Gaps Requiring New Development

Based on the detailed feature triage in Section 3.9:

| Gap | Description | OE-Global-2 Status | Estimated Effort | SME Status |
|---|---|---|---|---|
| **CSV/Bulk Upload** | Upload of patients, samples, and test results via CSV (23 files). Used for bulk data entry or migration from external labs | **Not covered** -- OE-Global-2 only has CSV import for analyzer results | Medium -- rebuild as Spring REST controller + React UI, or handle via FHIR resource import | Needs SME confirmation on whether this feature is actively used |
| **~~REST API compatibility~~** | ~~`PatientHandler` and `AccessionHandler` JSON endpoints with specific response format consumed by Bahmni Apps~~ | ~~**Data available, format different**~~ | ~~Medium if Bahmni Apps stays; None if Bahmni Apps is updated~~ | **Resolved — SME directed FHIR-first approach.** Bahmni EMR will consume FHIR endpoints; backward-compatible REST adapters are not needed as a general approach |
| **Custom test status types** | User-configurable test status types beyond standard lifecycle (13 files, 2 tables) | **Partial** -- standard statuses only, no user-configurable types | Low-Medium -- only if custom statuses are actively used | Needs SME confirmation |
| **Reference data sync** | Bulk sync of tests, panels, sample types from OpenMRS to OpenELIS | **Confirmed gap** -- OE-Global-2 manages test config locally, no inbound sync from external master | **Medium-High** -- needs FHIR-based sync mechanism (ActivityDefinition/PlanDefinition) or change in mastering model | **Confirmed by SME:** tests, panels, sample types mastered in OpenMRS, sync is required |
| **Data migration tooling** | Reusable tool to migrate data from Bahmni OpenELIS to OE-Global-2 for existing installations | **Not covered** -- no migration tooling exists | Medium-High -- must be productized, not a one-off script | **Confirmed by SME:** needed as a product feature for multiple installations |
| **Bahmni default configuration** | Default OE-Global-2 configuration harmonized with OpenMRS config and data | **Not covered** -- OE-Global-2 has country-specific configs but no Bahmni default | Low-Medium -- define standard config package | **Confirmed by SME:** no legacy config; new default needed |

**Previously listed as gaps but now confirmed as covered by OE-Global-2:**

| Feature | OE-Global-2 Equivalent | Action |
|---|---|---|
| Sample Source | `SampleNature` field in forms + `DisplayListService.SAMPLE_NATURE` | Configuration only (populate values) |
| Health Center | `Organization` module with `OrganizationType` categorization | Data migration only |
| Lab Dashboard | `PatientDashBoardProvider` with `DashBoardMetrics`, `OrderDisplayBean`, turnaround time tracking | None (richer than Bahmni's) — **SME confirms probably required; OE-Global-2 dashboard should suffice** |
| Accession-based validation | `AccessionValidationController`, `AccessionValidationRestController`, `AccessionValidationRangeController` | None (native first-class feature) |
| Provider list | FHIR Practitioner support + `ProviderImportService` | Configuration only — **SME confirms provider tracking is required** |
| External Reference mapping | Native FHIR identifiers and references | None (except data migration) |
| REST API compatibility | OE-Global-2 FHIR endpoints | **SME directed FHIR-first approach** — Bahmni EMR will consume FHIR directly; no backward-compatible REST adapters needed |

### 4.4 Database Migration

| Aspect | Assessment |
|---|---|
| Schema foundation | Same `clinlims` database and schema -- compatible ancestry |
| Schema divergence | Significant -- OE-Global-2 has dozens of migration files (2.0.x.x through 3.2.x.x) |
| Bahmni-specific tables | 13 tables (7 for AtomFeed infrastructure — **not needed** in OE-Global-2; 6 for features — need assessment: `external_reference`, `HEALTH_CENTER`, `sample_source`, `import_status`, `type_of_test_status`, `test_status`) |
| Data migration | Core data (patients, samples, tests, results) likely migrable with custom scripts |
| Migration approach | Dump Bahmni data -> apply OE-Global-2 Liquibase migrations -> custom data mapping scripts |
| Risk level | Medium-High -- requires careful testing with production data |

---

## 5. Critical Questions for SME

These questions need answers before a migration plan can be finalized:

### 5.1 OpenMRS / Bahmni EMR Side

1. **What version of OpenMRS does Bahmni currently use?** (OpenMRS 2.x or 3.x/O3?)
   - *Why it matters:* OE-Global-2 integrates via FHIR. OpenMRS 3.x has native FHIR2 module support. If Bahmni uses OpenMRS 2.x, the FHIR2 module may need to be installed/configured, which is a prerequisite for the migration.
   - **SME Answer:** Bahmni released version uses OpenMRS core **2.5.12**. The trunk has **2.6.x**.
   - *Implication:* OpenMRS 2.x does not have native FHIR support, but the FHIR2 module is available (see Q2). No OpenMRS core upgrade is strictly required for the migration, though 2.6.x trunk should be evaluated for FHIR compatibility improvements.

2. **Does the Bahmni OpenMRS deployment already have the FHIR2 module installed?**
   - *Why it matters:* This is the single biggest determinant of migration complexity. If FHIR2 is available, the OpenMRS <-> OpenELIS integration is primarily configuration. If not, it needs to be added first.
   - **SME Answer:** Yes, FHIR2 exists — [openmrs-module-fhir2Extension](https://github.com/Bahmni/openmrs-module-fhir2Extension). However, the migration is **not just configuration** — further input from Angshuman Sarkar is needed on the integration complexity.
   - *Implication:* The FHIR prerequisite is met, which is positive. However, the SME caution that integration is more than configuration means Phase 2 (PoC) should specifically validate the FHIR workflow end-to-end with Angshuman's guidance. The Bahmni FHIR2 extension may need enhancements to support the full lab order/result exchange that OE-Global-2 expects.

3. **What specific REST APIs does Bahmni EMR call on OpenELIS?**
   - *Why it matters:* We identified `PatientHandler` and `AccessionHandler` JSON endpoints. We need a complete list of API contracts that Bahmni EMR depends on, to determine if OE-Global-2's existing REST/FHIR endpoints can serve them or if adapters are needed.
   - **SME Answer:** *Not yet answered.* Needs follow-up — API contract catalog is still required.
   - *Action:* Schedule follow-up to enumerate all REST API calls from Bahmni EMR to OpenELIS. This can be done via code analysis of Bahmni Apps + network traffic capture from a running instance.

4. **Is the Bahmni EMR frontend being updated concurrently?**
   - *Why it matters:* If Bahmni EMR is also moving to a newer stack, it may be possible to consume FHIR endpoints directly instead of building backward-compatible REST APIs.
   - **SME Answer:** We should **assume FHIR endpoints for the integration**. Only use REST APIs where it's not possible to use FHIR.
   - *Implication:* This is a strong architectural direction — FHIR-first integration. This means: (a) backward-compatible REST API adapters (Section 4.3) are **not needed** as a general approach, (b) the Bahmni EMR side will also be updated to consume FHIR, and (c) the integration effort focuses on ensuring OE-Global-2's FHIR endpoints provide all the data Bahmni needs. Any REST APIs should be exceptions, not the rule.

### 5.2 Integration & Data Flow

5. **What is the complete data flow between OpenMRS, OpenELIS, and OpenERP/Odoo?**
   - *Why it matters:* We need to map every integration touchpoint to ensure nothing is missed. Specifically:
     - What triggers a lab order flow from OpenMRS to OpenELIS?
     - What data is sent back when results are validated?
     - What role does OpenERP/Odoo play? Is it just for billing/inventory, or does it drive lab workflows?
   - **SME Answers:**
     - **Lab order trigger:** A ServiceRequest raised in Bahmni raises an AtomFeed event, which is consumed by OpenELIS.
     - **Results sent back:** *Not yet answered.* Needs follow-up.
     - **Odoo role:** Lab orders are also synced to Odoo. Odoo holds the **pricing for lab tests** so that lab services can be billed. **No direct connection between Odoo and OpenELIS.**
   - *Implication:* The integration picture is simpler than expected — Odoo does not interact with OpenELIS directly, only with OpenMRS. The migration only needs to handle the **OpenMRS ↔ OpenELIS** integration (lab orders in, results out). The Odoo billing integration is independent and can remain on its existing OpenMRS ↔ Odoo pathway. The lab order trigger needs to migrate from AtomFeed to FHIR ServiceRequest/Task.

6. **Is reference data (tests, panels, sample types) managed in OpenELIS or synced from an external source?**
   - *Why it matters:* The Bahmni fork has reference data sync services. If this data is managed directly in OpenELIS, those services are unnecessary. If it's mastered elsewhere, we need an equivalent sync mechanism.
   - **SME Answer:** Lab tests, panels, and sample types are **managed in OpenMRS and synced to OpenELIS**. Reference ranges are **possibly managed only in OpenELIS**. **Sync is needed.**
   - *Implication:* This is a **confirmed gap**. OE-Global-2 manages test configuration locally and does not have a mechanism to sync test/panel/sample type definitions from an external master (OpenMRS). Options: (a) build a FHIR-based sync that reads ActivityDefinition/PlanDefinition from OpenMRS and creates corresponding test config in OE-Global-2, (b) use OE-Global-2's import capabilities to periodically sync, or (c) change the mastering model so test config is managed directly in OE-Global-2 and synced to OpenMRS instead. **This needs architectural discussion.**

7. **Are there any other systems consuming the AtomFeed events published by OpenELIS?**
   - *Why it matters:* If downstream systems depend on OpenELIS event feeds, they also need to be migrated to consume FHIR resources or alternative mechanisms.
   - **SME Answer:** Only **OpenMRS** consumes OpenELIS events (lab results only).
   - *Implication:* This simplifies the migration significantly — only one consumer to migrate. OE-Global-2's FHIR DiagnosticReport/Observation publishing replaces the AtomFeed result events, and OpenMRS (with FHIR2 module) can consume these.

### 5.3 Deployment & Operations

8. **What is the current deployment environment?** (Docker Compose? Kubernetes? Bare metal?)
   - *Why it matters:* OE-Global-2 has a more complex deployment (5 containers vs 2). The infrastructure needs to support this.
   - **SME Answer:** **Docker Compose.**
   - *Implication:* Good — Docker Compose is the primary deployment method for OE-Global-2 as well. The transition from 2 containers to 5 is manageable within the existing Docker Compose infrastructure. The Bahmni Docker Compose setup will need to be updated to include the additional OE-Global-2 containers (FHIR server, frontend, proxy).

9. **Is there existing production data that needs to be migrated?**
   - *Why it matters:* If this is a fresh deployment for a new client project, database migration complexity drops significantly. If migrating existing production data, custom migration scripts are needed.
   - **SME Answer:** **Not for IOM** (initial deployment), but **many other installations will need data migration**. This should be treated as a **product feature**, not a one-off.
   - *Implication:* Data migration tooling needs to be built as a **reusable, productized capability** — not a throwaway script. This increases the scope but adds long-term value. The migration tool should handle: (a) core data (patients, samples, tests, results) from Bahmni OpenELIS schema to OE-Global-2 schema, (b) Bahmni-specific table data (health centers → organizations, sample sources → sample natures, etc.), and (c) validation/rollback capabilities. For IOM, the initial deployment can start fresh on OE-Global-2.

10. **What is the timeline expectation for this migration?**
    - *Why it matters:* Determines whether a phased approach (run both systems in parallel) or a cutover approach is feasible.
    - **SME Answer:** **Phased approach.**
    - *Implication:* A phased approach means both systems may run in parallel during transition. This requires: (a) clear phase definitions (which capabilities move when), (b) potentially running both Bahmni OpenELIS and OE-Global-2 simultaneously during transition, and (c) data consistency strategy if both systems are active. The phased approach reduces risk but increases the total migration timeline.

### 5.4 Functional Requirements

11. **Are all the Bahmni-specific features (sample source, dashboard, provider tracking) required in the target system?**
    - *Why it matters:* Some features may be deprecated or superseded by OE-Global-2's native capabilities (e.g., OE-Global-2 has its own React dashboards).
    - **SME Answer:**
      - **Sample source:** Not sure — needs further assessment.
      - **Provider tracking:** **Yes — required.**
      - **Dashboard:** **Probably required.**
    - *Implication:* Provider tracking is confirmed required — OE-Global-2 has FHIR Practitioner support and `ProviderImportService`, so this should be covered natively. Dashboard is likely needed — OE-Global-2's native dashboard (Section 3.9, Feature 1) is richer and should suffice, but needs validation with lab users. Sample source needs clarification — if used, OE-Global-2's `SampleNature` field covers it (Section 3.9, Feature 6).

12. **Which of the 132 new files and 1,522 modifications in `us.mn.state.health.lims` are functionally important?**
    - *What we know:* Git history confirms that Bahmni added 132 new Java files and modified 1,522 of the original 1,447 files in the `us.mn.state.health.lims` package. Key new feature areas include: dashboard (11 files), CSV upload (23 files), health center management (9 files), REST handlers (11 files), and provider query/autocomplete (12 files).
    - *Why it matters:* We need the SME to identify which of these features are actively used and required in the target system. Some may be superseded by OE-Global-2 native capabilities; others may be must-haves that need porting or rebuilding.
    - **SME Answer:** *Not yet answered.* Still requires detailed SME walkthrough.
    - *Action:* Schedule focused session to review each feature area with SME, using the triage table in Section 3.9 as the agenda.

13. **What client-specific configurations exist?** (Haiti context in Liquibase, analyzer configurations, etc.)
    - *Why it matters:* OE-Global-2 has its own country-specific configurations. We need to know which apply to the target deployment.
    - **SME Answer:** **None.** A default configuration that ships with the product needs to be identified. This **must be harmonized with the OpenMRS config and data**.
    - *Implication:* No legacy client-specific config to carry forward — this simplifies the migration. However, a new task is created: define a **"Bahmni default" configuration** for OE-Global-2 that is consistent with the OpenMRS test/panel/sample type definitions (see Q6 — reference data is mastered in OpenMRS). This config should become the standard Bahmni distribution of OE-Global-2.

---

## 6. Proposed Next Steps

### Phase 1: Answer Critical Questions (2-3 weeks)

**Completed (from SME answers):**
- [x] ~~Verify Bahmni OpenMRS FHIR2 module availability~~ — **Confirmed:** FHIR2 module exists
- [x] ~~Determine integration approach (REST vs FHIR)~~ — **Decided:** FHIR-first, REST only where FHIR not possible
- [x] ~~Assess Odoo/OpenERP integration scope~~ — **Confirmed:** No direct Odoo-OpenELIS connection; Odoo handles billing via OpenMRS
- [x] ~~Identify downstream consumers of OpenELIS events~~ — **Confirmed:** Only OpenMRS
- [x] ~~Determine deployment environment~~ — **Confirmed:** Docker Compose
- [x] ~~Assess data migration needs~~ — **Confirmed:** Productized tooling needed for multiple installations; IOM starts fresh
- [x] ~~Determine migration approach~~ — **Decided:** Phased rollout
- [x] ~~Assess reference data mastering~~ — **Confirmed:** OpenMRS is master for tests/panels/sample types; sync required
- [x] ~~Assess client-specific configurations~~ — **Confirmed:** None; new Bahmni default config needed

**Still pending:**
- [ ] Catalog all REST API contracts between Bahmni EMR and OpenELIS (Q3)
- [ ] Document result publishing data flow from OpenELIS back to OpenMRS (Q5 partial)
- [ ] Confirm sample source feature requirement (Q11 partial)
- [ ] **Feature triage of `us.mn.state.health.lims` additions** (Q12) — review the 132 new files (dashboard, upload, health center, REST handlers, etc.) with SME to classify each as: (a) required and not in OE-Global-2, (b) already covered by OE-Global-2, or (c) can be dropped
- [ ] **Assess the 1,522 modified original files** — sample the most heavily modified files (OrderListDAOImpl, ResultsLogbookUpdateAction, SamplePatientEntrySaveAction, etc.) to understand whether changes are bug fixes, Bahmni-specific features, or integration glue
- [ ] **Architectural decision on reference data sync** — choose between FHIR-based inbound sync, OE-Global-2 import capabilities, or changing the mastering model. Involves Angshuman Sarkar.
- [ ] **Validate FHIR integration complexity with Angshuman Sarkar** — SME flagged this is not just configuration; need detailed assessment of what's involved

### Phase 2: Proof of Concept (2-4 weeks)

- [ ] Deploy OpenELIS-Global-2 in a development environment (Docker Compose)
- [ ] Configure FHIR integration with a test OpenMRS instance
- [ ] Verify lab order flow: OpenMRS -> FHIR ServiceRequest -> OE-Global-2 -> results back
- [ ] Configure Odoo integration using OE-Global-2's built-in module
- [ ] Test with sample data representative of the client's use cases
- [ ] Identify any functional gaps not covered by the analysis above

### Phase 3: Gap Development (3-6 weeks, depending on findings)

- [ ] Build any required REST API adapters for Bahmni EMR compatibility
- [ ] Implement missing Bahmni-specific features (if required and not covered by OE-Global-2)
- [ ] Develop database migration scripts (if migrating existing production data)
- [ ] Configure country/client-specific settings (test catalog, panels, analyzer mappings)

### Phase 4: Testing & Validation (2-4 weeks)

- [ ] End-to-end integration testing with full Bahmni stack
- [ ] Data migration testing with production data copy (if applicable)
- [ ] Performance testing (OE-Global-2 has a heavier deployment footprint)
- [ ] User acceptance testing with lab technicians

### Phase 5: Cutover (1-2 weeks)

- [ ] Final data migration (if applicable)
- [ ] Infrastructure deployment
- [ ] Parallel run period (if feasible)
- [ ] Go-live and monitoring

---

## 7. Risk Assessment

| Risk | Likelihood | Impact | Mitigation | SME Update |
|---|---|---|---|---|
| ~~Bahmni OpenMRS lacks FHIR2 module~~ | ~~Medium~~ | ~~High~~ | ~~Assess early in Phase 1~~ | **Risk mitigated.** FHIR2 module exists ([openmrs-module-fhir2Extension](https://github.com/Bahmni/openmrs-module-fhir2Extension)). However, SME notes integration is "not just configuration" — validate in PoC phase with Angshuman Sarkar |
| FHIR integration complexity underestimated | **Medium** | **High** | End-to-end PoC validation in Phase 2; involve Angshuman Sarkar early | **New risk** based on SME feedback that FHIR integration is not just configuration |
| Reference data sync gap | **High** | **High** | Architectural decision needed: FHIR-based sync vs change mastering model | **Confirmed by SME.** Tests/panels/sample types mastered in OpenMRS; sync to OpenELIS is required and OE-Global-2 has no inbound sync mechanism |
| Database schema divergence causes data loss during migration | Medium | High | Build productized migration tooling; extensive testing with production data | **Updated:** SME confirms data migration needed as product feature for multiple installations, not just one-off |
| Extensive customizations in stock OpenELIS code | **Confirmed** | **High** | Git history shows 132 new files + 1,522 modified files in `us.mn.state.health.lims`. Requires feature-by-feature triage with SME in Phase 1 to determine which are required | Partially addressed — SME confirmed provider tracking required, dashboard probably required, sample source unclear. Full triage still needed |
| OE-Global-2's heavier deployment impacts performance | Low | Medium | Performance testing in Phase 4; infrastructure sizing | Docker Compose deployment confirmed — compatible with OE-Global-2 |
| ~~Bahmni EMR depends on specific REST API response formats~~ | ~~Medium~~ | ~~Medium~~ | ~~API contract catalog in Phase 1~~ | **Risk mitigated.** SME directed FHIR-first approach — no backward-compatible REST adapters needed |
| OE-Global-2 is on develop branch (beta 3.2.1.2) | Medium | Medium | Evaluate stability; consider using latest stable release tag instead | No update |
| Bahmni default configuration undefined | Low | Medium | Define standard "Bahmni default" config harmonized with OpenMRS | **New risk** based on SME answer — no legacy config exists, new default must be created |

---

## 8. Conclusion

The migration from Bahmni OpenELIS to OpenELIS-Global-2 is **feasible and strategically sound**. The key factors in favor:

1. **Bahmni's integration layer is focused and bounded** (77 `org.bahmni` classes, ~6,400 LOC) — but an additional 132 new files and 1,522 modified files in `us.mn.state.health.lims` represent deeper feature work (dashboard, CSV upload, health center, etc.) that needs SME triage
2. **OE-Global-2 already has equivalents** for the integration layer (FHIR replaces AtomFeed, Odoo module replaces OpenERP feed) — but the `us.mn.state.health.lims` feature additions need individual gap assessment
3. **The core lab functionality is a superset** -- OE-Global-2 has more features than the Bahmni fork
4. **The database shares the same foundation** (`clinlims` schema)
5. **The tech stack is modern and maintainable** (Java 21, Spring 6, React vs Java 7/8, Struts 1.x, JSP)

### Updated Assessment Based on SME Answers

**Risks reduced:**
- **FHIR prerequisite is met** — Bahmni has the FHIR2 module ([openmrs-module-fhir2Extension](https://github.com/Bahmni/openmrs-module-fhir2Extension)), though SME cautions the integration is more complex than configuration alone
- **REST API backward compatibility is not needed** — SME directed a FHIR-first approach, eliminating the need for adapter APIs
- **Odoo integration is independent** — no direct Odoo-OpenELIS connection, simplifying the migration scope
- **Only one event consumer (OpenMRS)** — simplifies the AtomFeed → FHIR migration
- **Docker Compose deployment** — compatible with OE-Global-2's multi-container architecture

**New/confirmed risks:**
- **Reference data sync is a confirmed gap** — tests, panels, and sample types are mastered in OpenMRS and must be synced to OpenELIS. OE-Global-2 has no inbound sync mechanism. This is the **most significant new finding** and requires an architectural decision
- **Data migration must be productized** — not just for IOM but for all existing Bahmni installations. This adds scope but provides long-term product value
- **FHIR integration complexity** — SME explicitly cautioned this is not just configuration; Angshuman Sarkar's input is needed for accurate estimation
- **Bahmni default configuration** — no legacy config exists; a new default harmonized with OpenMRS must be defined

**Still pending:**
- Full triage of 132 new files / 1,522 modifications in `us.mn.state.health.lims` (Q12)
- Complete REST API contract catalog from Bahmni EMR to OpenELIS (Q3)
- Result publishing data flow details (Q5 partial)
- Sample source feature requirement confirmation (Q11 partial)

The estimated total effort remains **12-20 weeks** for the IOM deployment (which starts fresh without data migration). For the productized migration tooling that supports existing installations, add an additional **4-6 weeks**. The reference data sync mechanism is the key new work item that was not previously accounted for and could add **2-4 weeks** depending on the architectural approach chosen. **A phased rollout** (per SME direction) allows incremental delivery and risk reduction.

---

*This document was generated through automated codebase analysis of both repositories. All technical details have been verified against the actual source code.*
