# Repository: openmrs-module-fhir2

*Back to [Integration Plan](../../bahmni-openelis-global2-integration-plan.md)*

**Repo:** `openmrs/openmrs-module-fhir2`
**Local path:** `/Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/openmrs-module-fhir2`
**Deep dive date:** 2026-02-19
**Changes required:** None

---

## Role in Integration

`openmrs-module-fhir2` runs inside OpenMRS (as an OMOD). It exposes OpenMRS data as FHIR R4 resources over REST. The mediator uses it in two ways:
1. **Read** — fetch Patient, ServiceRequest (order) data to build FHIR bundles for OEG2
2. **Write** — push DiagnosticReport + Observation results back into OpenMRS after OEG2 completes them

---

## Verified Findings

### Base Endpoint
```
/ws/fhir2/{resource}
```
Requires OpenMRS authentication (standard session or Basic Auth).

### FHIR Resources Available (R4)

All resources the mediator needs are fully supported:

| Resource | Mediator Use | Endpoint |
|---|---|---|
| `Patient` | Read patient data for FHIR bundle | `GET /ws/fhir2/Patient?identifier=<id>` |
| `ServiceRequest` | Read lab orders; poll for new via `_lastUpdated` | `GET /ws/fhir2/ServiceRequest?patient=<uuid>&_lastUpdated=gt<ts>` |
| `Task` | Read/write Task status | `GET/PUT /ws/fhir2/Task/{id}` |
| `DiagnosticReport` | Write results back from OEG2 | `POST /ws/fhir2/DiagnosticReport` |
| `Observation` | Write individual test results | `POST /ws/fhir2/Observation` |
| `Encounter` | Read encounter context | `GET /ws/fhir2/Encounter/{id}` |
| `Practitioner` | Read ordering provider | `GET /ws/fhir2/Practitioner/{id}` |

### Task Resource
Full CRUD + search supported. Relevant search parameters:
```
GET /ws/fhir2/Task?owner=<uuid>&status=completed&_lastUpdated=gte<ts>
GET /ws/fhir2/Task?based-on=<serviceRequestId>
GET /ws/fhir2/Task?subject=<patientUuid>
```

### ServiceRequest as Alternative Polling Mechanism
If `/bahmnicore/orders` lacks timestamp filtering, the mediator can use:
```
GET /ws/fhir2/ServiceRequest?_lastUpdated=gt<ISO-8601>&status=active
```
This avoids any bahmni-core changes entirely.

### Limitations Relevant to Integration

| Limitation | Impact | Mitigation |
|---|---|---|
| No push/subscription | Cannot notify mediator of new orders; must poll | Poll on schedule (acceptable) |
| No bulk insert | Must use Transaction Bundle for batches | Use FHIR Transaction Bundle (as labonfhir does) |
| No automatic Task state transitions | Task status does not self-advance | Mediator manages state explicitly |
| Pagination required | Large result sets return bundles with `next` links | Implement pagination loop in mediator |

---

## Changes Required

**None.** Already deployed in Bahmni's OpenMRS as a standard module. All required resources and endpoints are available.

---

## Open Questions (repo-specific)

| # | Question |
|---|---|
| 1 | Is `openmrs-module-fhir2` already installed and enabled in Bahmni's standard OpenMRS distribution? Needs to be confirmed before Phase 1 PoC. |
| 2 | When the mediator writes a `DiagnosticReport` + `Observation` via `/ws/fhir2/`, do those observations automatically appear in `GET /bahmnicore/labOrderResults`? The result display chain needs to be traced end-to-end. |
| 3 | Does Bahmni's OpenMRS version satisfy the fhir2 minimum requirement (OpenMRS 2.6.0+)? |

---

## Key Files

| File | Purpose |
|---|---|
| `api/src/.../providers/r4/TaskFhirResourceProvider.java` | Task CRUD + search implementation |
| `api/src/.../providers/r4/ServiceRequestFhirResourceProvider.java` | ServiceRequest (order) API |
| `api/src/.../providers/r4/PatientFhirResourceProvider.java` | Patient resource |
| `api/src/.../providers/r4/DiagnosticReportFhirResourceProvider.java` | DiagnosticReport write |
| `api/src/.../providers/r4/ObservationFhirResourceProvider.java` | Observation write |
| `api/src/.../api/FhirTaskService.java` | Task service interface |
| `omod/src/main/resources/config.xml` | Servlet configuration, endpoint registration |
