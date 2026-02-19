# Decision Log: Bahmni + OE-Global-2 Integration

*Back to [Integration Plan](../bahmni-openelis-global2-integration-plan.md)*

This log captures key decisions and research findings made during the design process.

---

## Decisions

| # | Decision | Rationale | Source |
|---|---|---|---|
| 1 | **Architecture: Simplified** — mediator pushes directly to OE-Global-2's FHIR store. No OpenHIM/SHR. | OpenHIM is a heavyweight interop layer for HL7v2/CDA translation — unnecessary for pure FHIR. Halves container count (7 vs 12). Path to Full OpenHIE is additive if needed later. | SME meeting (2026-02-18) |
| 2 | **Custom mediator service** instead of `openmrs-module-labonfhir` | Consistent with Bahmni's existing integration patterns (ERP/PACS — standalone microservice). Allows backward compatibility with old OpenELIS. `labonfhir` runs inside OpenMRS and has Bahmni Order structure incompatibilities. | SME meeting (2026-02-18); repo deep dive (2026-02-19) |
| 3 | **Master data ownership:** OpenMRS for test definitions, OEG2 for reference ranges | Reference ranges vary by ethnicity, location — lab systems handle this better. Test definitions are mastered where orders originate. | SME meeting (2026-02-18) |
| 4 | **Patient sync: immediate** on creation, not on-demand with orders | Some doctors order on paper — patient must already exist in the lab. Reverse sync (lab → OpenMRS) deferred. | SME meeting (2026-02-18) |
| 5 | **Data migration: not required** | Existing installations keep old OpenELIS. New installations use OEG2. Patient migration via event replay script. Old results stay in old system. | SME meeting (2026-02-18) |
| 6 | **LOINC codes: Bahmni will be updated** | OEG2 requires LOINC codes for test matching. Bahmni's test catalog will be updated with LOINC codes as needed. | SME meeting (2026-02-18) |
| 7 | **Order detection: AtomFeed encounter feed** — mediator subscribes to `openmrs/ws/atomfeed/encounter/recent` | Same feed the old OpenELIS and pacs-integration consume. Event-driven, proven in production, no new OpenMRS endpoints needed. REST polling was considered but AtomFeed is the established Bahmni integration pattern. | Repo deep dive + flow diagram analysis (2026-02-19) |
| 8 | **Mediator structure: modelled on `pacs-integration`** | pacs-integration is a production-deployed Bahmni standalone mediator doing the same job (AtomFeed → downstream system). Uses `org.ict4h.atomfeed.client` v1.10.1, Quartz scheduling, PostgreSQL cursor/retry tracking. Reusing the pattern reduces risk and is consistent with the codebase. | pacs-integration repo deep dive (2026-02-19) |
| 9 | **OEG2 requires no code changes** — configuration only | OEG2's FHIR polling, Task lifecycle, DiagnosticReport push, and auth are all configurable via `common.properties`. No modifications needed. | OEG2 repo deep dive (2026-02-19) |
| 10 | **`openmrs-module-fhir2` used for result write-back** — mediator pushes DiagnosticReport + Observations to OpenMRS via `/ws/fhir2/` | fhir2 is already deployed in Bahmni's OpenMRS and exposes full CRUD for all needed resources (Task, ServiceRequest, Patient, DiagnosticReport, Observation). No additional OpenMRS modules needed. | openmrs-module-fhir2 repo deep dive (2026-02-19) |

---

## Research Findings

Key findings from community research, diagram analysis, and code analysis that informed the decisions above.

| Finding | Source |
|---|---|
| FHIR2 module alone doesn't create Tasks — an active bridge is required to push FHIR bundles to the lab | [Community discussion](https://talk.openelis-global.org/t/integration-with-openmrs-over-fhir/1702) |
| Exchange between OpenMRS and OE-Global-2 is purely FHIR R4 — no HL7v2 | [Moses Mutesasira](https://talk.openelis-global.org/t/integration-with-openmrs-over-fhir/1702/2) |
| OE-Global-2 matches tests by LOINC codes only — no fallback to custom codes | OEG2 code analysis (2026-02-19) |
| Test methods (PCR, culture, etc.) are selected at execution time by the lab tech, not at order time. LOINC mapping is test-level, not method-level. | [Community discussion](https://talk.openelis-global.org/t/openelis-global-capability-for-selecting-a-specific-method-for-a-given-order/1691) |
| A working reference implementation exists ([DIGI-UW/openelis-openmrs-hie](https://github.com/DIGI-UW/openelis-openmrs-hie)) with Docker Compose | GitHub |
| `openmrs-module-labonfhir` runs inside OpenMRS (not standalone) and uses internal Spring events — cannot be used as-is by an external mediator. Bahmni Order structure also incompatible. | labonfhir repo deep dive (2026-02-19) |
| OpenMRS AtomFeed is **bidirectional**: OpenMRS publishes encounter events outbound (old OpenELIS and PACS integration subscribe); old OpenELIS publishes result events inbound (bahmni-core consumes). Not inbound-only as initially assumed. | Flow diagram analysis (2026-02-19) |
| `pacs-integration` uses `org.ict4h.atomfeed.client` v1.10.1 — the exact library the new mediator should use. Quartz scheduling, PostgreSQL `markers` table for cursor, `failed_events` table for retry queue. | pacs-integration repo deep dive (2026-02-19) |
| OEG2 `external-fhir-api` is HAPI FHIR v6.6.0 (Tomcat). OEG2 polls it every 120s for Tasks with `status=REQUESTED`. Patient `birthDate` and `gender` are required fields — OEG2 rejects bundles without them. | OEG2 repo deep dive (2026-02-19) |
| The lab order UI lives in `openmrs-module-bahmniapps` (Angular), not `bahmni-frontend` (React micro-frontend which only has medication so far). | bahmni-frontend + archDiagWithRepoLinks.svg analysis (2026-02-19) |
| `openmrs-module-bahmniapps` is fully decoupled from OpenELIS. Zero direct calls to OpenELIS — no hardcoded URLs, no "Open in lab" links, no order status display. All lab APIs go through `/openmrs/ws/rest/v1/bahmnicore/`. **No changes required to this repo.** | bahmniapps deep dive (2026-02-19) |
| **`bahmni-reports` impact is narrower than the arch diagram implied.** Only 2 of 45 report types query OpenELIS: `TestCount` (queries `test_section`, `test`, `analysis`, `result`, `test_result`; shows result counts by department/test) and `ElisGeneric` (user-provided SQL). Five lab report types (`GenericLabOrderReport`, `PatientsWithLabtestResults`, `GenericObservationReport`, `GenericObservationFormReport`, `OrderFulfillmentReport`) use `@UsingDatasource("openmrs")` and are unaffected — they query OpenMRS obs, which the mediator populates. `TestCount` must be rewritten against OpenMRS observations (preferred) or OEG2 schema. | bahmni-reports repo deep dive (2026-02-19) |
| **`bahmni-metabase` does NOT connect to OpenELIS PostgreSQL.** The arch diagram connection was incorrect or refers to a different deployment. Metabase connects only to OpenMRS MySQL and Bahmni Mart PostgreSQL. No lab dashboards exist — all 4 predefined reports are patient/clinical analytics only. Zero impact from OEG2 migration. (Closes former Open Question 8.) | bahmni-metabase repo deep dive (2026-02-19) |
| **Billing gap (Q4) is more concrete than previously noted:** `billingFlow.png` shows OpenELIS publishes dedicated "test catalog events" via AtomFeed → Odoo Connect → syncs lab test products to Odoo. This is a separate, explicit feed — not incidental. OEG2 has no equivalent mechanism. | billingFlow.png review (2026-02-19) |
| **`openmrs-distro-bahmni` is a Maven/Docker image build repo, not a Docker Compose orchestration repo.** It builds only the OpenMRS container image and its Helm chart. OpenELIS integration is embedded in the image via: `openelis-atomfeed-client-omod` + `elis-fhir-result-support-omod` (pom.xml), `bahmnicore.properties.template` (OpenELIS URI + credentials), `update_elis_host_port.sh` (startup script updating AtomFeed markers in OpenMRS DB), and Helm chart env vars (`OPENELIS_HOST`, `OPENELIS_PORT`, `OPENELIS_ATOMFEED_USER/PASSWORD`). All must be removed. The full multi-container Bahmni stack (where OEG2 containers must be added) is in a different repo — not yet identified. | openmrs-distro-bahmni repo deep dive (2026-02-19) |
| `bahmnicore/labOrderResults` response fields consumed by the UI: `accessionUuid`, `accessionDateTime`, `accessionNotes`, `testName`, `panelName`, `result`, `resultDateTime`, `minNormal`, `maxNormal`, `testUnitOfMeasurement`, `abnormal`, `referredOut`, `uploadedFileName`. The mediator's result write-back must ensure these fields are populated in OpenMRS observations. | bahmniapps deep dive (2026-02-19) |
| No order status is shown in the Bahmni UI (no pending/in-progress/completed tracking). The Task lifecycle (REQUESTED → ACCEPTED → IN_PROGRESS → COMPLETED) is invisible to clinicians — it exists only between the mediator, FHIR store, and OEG2. | bahmniapps deep dive (2026-02-19) |
| Billing: OpenELIS publishes lab test catalog events to Odoo via AtomFeed → Odoo Connect. OEG2 has no equivalent feed. This is an integration gap that must be addressed before go-live. | billingFlow.png diagram analysis (2026-02-19) |
| OEG2 Task lifecycle: REQUESTED → ACCEPTED (or REJECTED) → RECEIVED → IN_PROGRESS → COMPLETED. Status updates are pushed back to `external-fhir-api` by OEG2 at each step. | OEG2 `FhirApiWorkFlowServiceImpl` code analysis (2026-02-19) |
