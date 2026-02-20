# Bahmni + OpenELIS-Global-2: Integration Plan

**Date:** 2026-02-17 (Updated: 2026-02-19)
**Status:** Draft (SME-reviewed 2026-02-18; repo deep dive completed 2026-02-19)
**Objective:** Replace Bahmni's OpenELIS fork with OpenELIS-Global-2 (OE-Global-2), integrated via FHIR.

**Detail pages:** [Current Flow](docs/current-flow-detail.md) | [Proposed Flow](docs/proposed-flow-detail.md) | [Architecture](docs/architecture-detail.md) | [Technical Reference](docs/technical-reference.md)

**Supporting pages:** [Decisions Log](docs/decisions-log.md) | [Fallback: Full OpenHIE](docs/fallback-option-a.md) | [Mediator Service Design](docs/mediator-service-design.md)

**Repository analysis:** [OpenELIS-Global-2](docs/repos/openelis-global-2.md) | [bahmni-core](docs/repos/bahmni-core.md) | [openmrs-module-fhir2](docs/repos/openmrs-module-fhir2.md) | [openmrs-module-labonfhir](docs/repos/openmrs-module-labonfhir.md) | [openmrs-module-atomfeed](docs/repos/openmrs-module-atomfeed.md) | [openmrs-module-bahmniapps](docs/repos/openmrs-module-bahmniapps.md) | [bahmni-frontend](docs/repos/bahmni-frontend.md) | [pacs-integration](docs/repos/pacs-integration.md) | [bahmni-reports](docs/repos/bahmni-reports.md) | [bahmni-metabase](docs/repos/bahmni-metabase.md) | [openmrs-distro-bahmni](docs/repos/openmrs-distro-bahmni.md) | [bahmni-mart](docs/repos/bahmni-mart.md)

---

## 1. Context

Bahmni ships a fork of OpenELIS (v3.1, circa 2013) integrated with OpenMRS via AtomFeed — a custom polling mechanism. OpenELIS-Global-2 is the actively maintained successor with native FHIR R4 support. We are adopting it **as-is** — no code porting, no forking.

**The work is integration:** making OE-Global-2 and Bahmni's OpenMRS exchange lab orders, results, and reference data via FHIR.

---

## 2. Current vs Proposed: At a Glance

| Aspect | Current (AtomFeed) | Proposed (FHIR) |
|---|---|---|
| **Order detection** | OpenELIS polls OpenMRS AtomFeed encounter feed | Mediator subscribes to same AtomFeed encounter feed |
| **Order push** | OpenELIS fetches order via REST | Mediator creates FHIR Task + ServiceRequest + Patient → pushes to OE-Global-2's FHIR store |
| **Order pickup by LIS** | OpenELIS polls AtomFeed (5s) | OE-Global-2 polls its FHIR store (2 min) |
| **Test matching** | Custom code mapping | LOINC code lookup |
| **Result return** | OpenELIS publishes to own AtomFeed | OE-Global-2 pushes DiagnosticReport to its FHIR store |
| **Result pickup** | OpenMRS polls OpenELIS AtomFeed | Mediator polls FHIR store → pushes results to OpenMRS |
| **Patient sync** | AtomFeed (immediate) | Mediator pushes Patient resource (immediate) |
| **Lab UI** | Struts/JSP (2013 vintage) | React |
| **Integration standard** | Atom RFC 4287 (custom) | HL7 FHIR R4 |
| **Key integration component** | `openmrs-module-atomfeed`, `bahmni-core` | Custom mediator service (standalone container) |
| **Master data** | OpenMRS (tests + ranges) | OpenMRS (test definitions), OEG2 (reference ranges) |
| **Containers** | 2 (app + db) | 7 (2 replaced + 5 new) |

**What stays the same:** Bahmni UI order entry, lab work in LIS, Bahmni UI result display.

**Go-live blockers beyond the core integration:**
- **Billing (needs investigation):** `billingFlow.png` shows OpenELIS publishing test catalog events to Odoo, but code analysis found no such publisher in the deployed codebase. The actual billing sync mechanism needs to be confirmed before assessing whether a gap exists for OEG2. See [Open Question 3](#5-open-questions).
- **Reports:** `bahmni-reports` has two report types (`TestCount`, `ElisGeneric`) that query OpenELIS PostgreSQL directly and will break. Five other lab report types query OpenMRS and are unaffected. See [Open Question 6](#5-open-questions).

Detail: [Current Flow](docs/current-flow-detail.md) | [Proposed Flow](docs/proposed-flow-detail.md) | [Architecture](docs/architecture-detail.md)

---

## 3. How It Works

See [Proposed Flow Detail](docs/proposed-flow-detail.md) for the full sequence diagram and [Mediator Service Design](docs/mediator-service-design.md) for the implementation design.

A **custom mediator service** bridges OpenMRS and OE-Global-2. It subscribes to OpenMRS's AtomFeed encounter feed, detects lab orders in new encounters, constructs FHIR bundles, and pushes them to OE-Global-2's FHIR store (`external-fhir-api`). Results flow back via a polling loop: the mediator polls `external-fhir-api` for completed Tasks and pushes results into OpenMRS via its FHIR API.

This is the same pattern used by the PACS integration and the old OpenELIS — a standalone Spring Boot service consuming the OpenMRS encounter feed. The mediator is modelled directly on `pacs-integration`.

```mermaid
flowchart LR
    MRS[OpenMRS\nAtomFeed] -->|encounter events| MED[Mediator<br/>Service]
    MED -->|push FHIR bundle| FHIR[OE-Global-2's<br/>FHIR store<br/>external-fhir-api]
    MED -->|push results back| MRS
    OEG[OE-Global-2] <-->|poll orders<br/>push results| FHIR
```

**Backward compatibility:** The mediator only handles OEG2 integration. The existing AtomFeed-based OpenELIS integration continues to work independently. Both can coexist — deployments choose which LIS to use via a feature flag in the mediator.

---

## 4. Architecture

OE-Global-2 ships with a HAPI FHIR server (`external-fhir-api`) as a standard container. The mediator pushes FHIR bundles directly to this store — no intermediary infrastructure needed. OE-Global-2 polls the same store for new orders.

See [Architecture Detail](docs/architecture-detail.md) for the full container diagram, container list, and configuration.

**7 containers total** — 2 replace existing OpenELIS containers (webapp + database), 5 are net new (FHIR store, frontend, proxy, certs, mediator service).

*A [Full OpenHIE fallback](docs/fallback-option-a.md) exists if auth/audit requirements emerge — adds OpenHIM + SHR (6 extra containers). The path is additive.*

---

## 5. Open Questions

| # | Question | Blocks | Owner |
|---|---|---|---|
| 1 | What are ALL AtomFeed interactions between Bahmni OpenELIS and OpenMRS? Beyond lab orders and results — patient sync, test catalog, metadata. Needed to confirm nothing is missed. | Phase 1 | Angshuman Sarkar |
| 2 | How does the Bahmni data model map to OEG2's data model? Specific gaps: sample source (OPD/IPD — custom to Bahmni), requester (ordering doctor), location in FHIR Task. | Phase 2 | Team |
| 3 | **Billing — mechanism unclear, needs investigation.** `billingFlow.png` shows OpenELIS publishing "test catalog events" to Odoo Connect via AtomFeed. However, code analysis of OpenELIS found NO outbound test catalog publisher: the `openerp.labtest.feed.uri` property is a consumer URL (OpenELIS reading from Odoo) and is absent from the Docker template; no reader job class exists in `LateStartScheduler.java`; OpenELIS only publishes `patient` and `accession` feeds. The diagram may show an aspirational flow or a non-Docker deployment variant. **Before assuming a gap, confirm:** (a) how lab test products actually reach Odoo in live Bahmni+Odoo deployments; (b) whether the test catalog sync originates from OpenMRS reference data rather than OpenELIS. The gap for OEG2 depends entirely on the answer. | Blocks Phase 4 / Go-Live | Team + Odoo owners |
| 4 | `Task.owner` in the FHIR bundle must match OEG2's `remote.source.identifier` config. What identifier scheme and value to use? Needs alignment during Phase 1 PoC. | Phase 1 | Team |
| 5 | `bahmni-core`'s `labOrderResults` endpoint: does it populate `minNormal`, `maxNormal`, `testUnitOfMeasurement`, `referredOut`, `uploadedFileName` from OpenMRS observation data written by the mediator, or from OpenMRS concept numeric limits? If from concept limits, OEG2-specific reference ranges will not appear in the Bahmni UI. Must be verified in Phase 1 PoC. | Phase 1 / Result Display | Team |
| 8 | **Docker Compose orchestration repo not yet identified.** `openmrs-distro-bahmni` only builds the OpenMRS Docker image — it is not the full stack orchestration repo. OEG2 containers (webapp, DB, FHIR store, frontend, proxy, certs) and the mediator must be added to whichever repo defines the full Bahmni container stack. What is that repo? (Likely `bahmni-docker` or `bahmni-devops` or similar.) | Blocks Phase 4 / Go-Live | Team |
| 6 | **`bahmni-reports` TestCount gap:** Deep dive confirmed only 2 report types query OpenELIS: `TestCount` (queries `test_section`, `test`, `analysis`, `result`, `test_result` — shows result counts by department/test) and `ElisGeneric` (user-provided SQL against `clinlims`). Both will break with OEG2. The 5 lab report types that query OpenMRS (`GenericLabOrderReport`, `PatientsWithLabtestResults`, `GenericObservationReport`, `GenericObservationFormReport`, `OrderFulfillmentReport`) are **unaffected**. Decision needed: rewrite `TestCount` against OEG2 schema (new datasource) or against OpenMRS observations (preferred — keeps a single authoritative source). See [bahmni-reports.md](docs/repos/bahmni-reports.md). | Blocks Phase 4 / Go-Live | Team |

---

## 6. Plan

### Phase 1: Proof of Concept (2-3 weeks)

**Goal:** Validate the FHIR integration end-to-end with OE-Global-2.

**Step 1a: Spin up OE-Global-2**
- [ ] Set up OE-Global-2 containers (webapp, database, external-fhir-api, frontend, proxy, certs)
- [ ] Configure OE-Global-2 to poll its `external-fhir-api` for orders
- [ ] Confirm `external-fhir-api` accepts writes from external clients

**Step 1b: Validate end-to-end FHIR flow**
- [ ] Push a FHIR Task + ServiceRequest + Patient bundle to `external-fhir-api` (manually or via script)
- [ ] Confirm OE-Global-2 picks up the order and creates a lab accession
- [ ] Enter and validate a result in OE-Global-2 → confirm DiagnosticReport is pushed back to `external-fhir-api`
- [ ] Observe the full Task lifecycle: REQUESTED → ACCEPTED → IN_PROGRESS → COMPLETED
- [ ] Agree on `Task.owner` / OEG2 `remote.source.identifier` value (open question 5)

**Step 1c: Confirm scope of AtomFeed interactions with Angshuman (open question 1)**
- [ ] Get full list of AtomFeed interactions to/from Bahmni OpenELIS
- [ ] Confirm nothing beyond lab orders, results, and test catalog sync is affected

**Step 1d: Confirm mediator service design**
- [ ] Review [Mediator Service Design](docs/mediator-service-design.md) — design is based on pacs-integration blueprint
- [ ] Confirm LOINC code mapping approach (config file vs DB table)
- [ ] Confirm patient sync approach (separate feed vs on-demand)
- [ ] Begin investigation of Odoo test catalog sync gap (open question 4)

*If the FHIR integration fails validation, fall back to the [Full OpenHIE approach](docs/fallback-option-a.md).*

### Phase 2: Test Catalog + LOINC (2-3 weeks)

Bahmni's test catalog must be updated with LOINC codes — OEG2 requires them for test matching.

- [ ] Audit Bahmni test catalog for LOINC code coverage
- [ ] Add LOINC codes to tests that don't have them
- [ ] Create CSV configuration files for OE-Global-2
- [ ] Validate order matching end-to-end

### Phase 3: Build Mediator Service (2-3 weeks)

Standalone Spring Boot service modelled on `pacs-integration`. See [Mediator Service Design](docs/mediator-service-design.md).

- [ ] Subscribe to OpenMRS AtomFeed encounter feed; filter for lab order encounters
- [ ] Implement FHIR bundle construction: Task (status=REQUESTED) + ServiceRequest + Patient
- [ ] Implement push to `external-fhir-api` (HAPI FHIR Transaction Bundle)
- [ ] Implement result polling: poll `external-fhir-api` for Tasks with `status=COMPLETED`
- [ ] Implement result import: push DiagnosticReport/Observations to OpenMRS via `/ws/fhir2/`
- [ ] Implement patient sync
- [ ] Implement retry queue and cursor tracking (from pacs-integration pattern)
- [ ] Add feature flag for mediator activation
- [ ] Unit and integration tests

### Phase 4: Master Data + Deployment (2-3 weeks)

- [ ] Configure master data (result ranges, organizations, providers, users)
- [ ] Resolve Odoo test catalog sync (open question 4)
- [ ] Remove OpenELIS AtomFeed integration from `openmrs-distro-bahmni` (remove `openelis-atomfeed-client-omod`, `update_elis_host_port.sh`, OpenELIS Helm env vars). See [openmrs-distro-bahmni.md](docs/repos/openmrs-distro-bahmni.md)
- [ ] Add OEG2 containers to the Bahmni Docker Compose orchestration stack (separate repo — not yet identified; **Open Question 9**)
- [ ] Configure networking, proxy, SSL, authentication
- [ ] Rewrite `TestCount` report (`bahmni-reports`) against OpenMRS observations; update any deployment-specific `ElisGeneric` SQL. See [bahmni-reports.md](docs/repos/bahmni-reports.md) (open question 6)
- [ ] `bahmni-metabase` — no changes required; has no OpenELIS connection. See [bahmni-metabase.md](docs/repos/bahmni-metabase.md)

### Phase 5: End-to-End Testing (2-3 weeks)

- [ ] Full lab workflow testing (order → sample → result → validation → report → display)
- [ ] Edge cases: rejected samples, amended results, cancelled orders
- [ ] User acceptance testing with lab technicians
- [ ] Verify Odoo billing integration end-to-end

### Phase 6: Go-Live (1 week)

- [ ] Deploy to production (fresh install, no data migration)
- [ ] Monitor for issues during initial operation period

**Total: 12-17 weeks**

### Data Migration

No full data migration is required.

- **Existing installations** continue using old OpenELIS — no forced migration.
- **New installations** use OEG2 from the start.
- **Patient migration** (when switching): replay patient creation events via a simple script.
- **Lab results:** Old results stay in old system — no result migration.

### Community Engagement

Once the high-level solution design is stable, present the plan to the Bahmni community:
- [ ] Create a Talk thread with the integration approach and architecture
- [ ] Solicit feedback from community members and implementers
- [ ] Consider a community call to walk through the design

---

## 7. Repository Analysis

Per-repo deep dives completed 2026-02-19 through 2026-02-20. Each file captures: role in the integration, verified findings, exact changes required, and key implementation files.

| Repo | Changes Required | Analysis |
|---|---|---|
| **OpenELIS-Global-2** | None — configuration only | [openelis-global-2.md](docs/repos/openelis-global-2.md) |
| **openmrs-module-fhir2** | None — all needed FHIR resources already exposed | [openmrs-module-fhir2.md](docs/repos/openmrs-module-fhir2.md) |
| **openmrs-module-atomfeed** | None — mediator subscribes to existing encounter feed | [openmrs-module-atomfeed.md](docs/repos/openmrs-module-atomfeed.md) |
| **bahmni-core** | None — mediator uses AtomFeed, not bahmni-core orders API | [bahmni-core.md](docs/repos/bahmni-core.md) |
| **openmrs-module-bahmniapps** | None — UI is fully decoupled from OpenELIS; no hardcoded LIS URLs, no "Open in lab" links, all API calls go through OpenMRS/bahmni-core | [openmrs-module-bahmniapps.md](docs/repos/openmrs-module-bahmniapps.md) |
| **bahmni-frontend** | None — new React micro-frontend has no lab UI yet | [bahmni-frontend.md](docs/repos/bahmni-frontend.md) |
| **openmrs-module-labonfhir** | Not deployed — FHIR bundle patterns used as reference | [openmrs-module-labonfhir.md](docs/repos/openmrs-module-labonfhir.md) |
| **pacs-integration** | Not modified — structural blueprint for mediator service | [pacs-integration.md](docs/repos/pacs-integration.md) |
| **bahmni-reports** | Changes needed — `TestCount` report type queries 5 clinlims tables (`test_section`, `test`, `analysis`, `result`, `test_result`) and must be rewritten against OpenMRS observations. `ElisGeneric` user SQL must be updated. Five OpenMRS-based lab report types are unaffected. | [bahmni-reports.md](docs/repos/bahmni-reports.md) |
| **bahmni-metabase** | None — no OpenELIS connection exists. Only connects to OpenMRS MySQL and Mart PostgreSQL. No lab dashboards present. Zero impact from OEG2 migration. | [bahmni-metabase.md](docs/repos/bahmni-metabase.md) |
| **openmrs-distro-bahmni** | Changes needed — this is the **OpenMRS image build repo** (Maven + Docker + Helm), not the full stack orchestration repo. Must remove: `openelis-atomfeed-client-omod`, OpenELIS properties from `bahmnicore.properties.template`, `update_elis_host_port.sh` startup script, and OpenELIS env vars from Helm chart. OEG2 container orchestration (full stack) is a separate task for the Docker Compose orchestration repo (not yet identified). | [openmrs-distro-bahmni.md](docs/repos/openmrs-distro-bahmni.md) |
| **bahmni-mart** | None — zero OpenELIS dependency. Sources exclusively from OpenMRS MySQL. No references to `clinlims` or any LIS tables anywhere in the codebase. Lab results written by the mediator into OpenMRS observations will flow into bahmni-mart's existing ETL jobs unchanged. | [bahmni-mart.md](docs/repos/bahmni-mart.md) |

---

## 8. References

| Source | Link | Relevance |
|---|---|---|
| **Reference implementation** | [github.com/DIGI-UW/openelis-openmrs-hie](https://github.com/DIGI-UW/openelis-openmrs-hie) | Working Docker Compose stack (OpenMRS 3 + OE-Global-2 + OpenHIM + SHR). Reference for OEG2 container setup and FHIR configuration. |
| **FHIR integration discussion** | [talk.openelis-global.org/t/1702](https://talk.openelis-global.org/t/integration-with-openmrs-over-fhir/1702) | Community discussion (Angshuman + Moses Mutesasira). Confirmed purely FHIR exchange, active bridge required. |
| **Test method selection** | [talk.openelis-global.org/t/1691](https://talk.openelis-global.org/t/openelis-global-capability-for-selecting-a-specific-method-for-a-given-order/1691) | Method selection at execution time; LOINC mapping is test-level, not method-level. |
| **Lab on FHIR module** | [github.com/openmrs/openmrs-module-labonfhir](https://github.com/openmrs/openmrs-module-labonfhir) | Reference for FHIR Task + ServiceRequest bundle construction. |

---

*Detail pages: [Current Flow](docs/current-flow-detail.md) | [Proposed Flow](docs/proposed-flow-detail.md) | [Architecture](docs/architecture-detail.md) | [Technical Reference](docs/technical-reference.md)*

*Supporting pages: [Decisions Log](docs/decisions-log.md) | [Fallback: Full OpenHIE](docs/fallback-option-a.md)*

*Archived analysis documents with detailed code inventory available in [archive/](archive/) for reference.*
