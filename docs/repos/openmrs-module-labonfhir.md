# Repository: openmrs-module-labonfhir

*Back to [Integration Plan](../../bahmni-openelis-global2-integration-plan.md)*

**Repo:** `openmrs/openmrs-module-labonfhir`
**Local path:** `/Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/openmrs-module-labonfhir`
**Deep dive date:** 2026-02-19
**Changes required:** Not deployed — reference pattern only

---

## Role in Integration

This module is **not deployed** in the Bahmni integration. It is a reference implementation that shows the proven patterns for FHIR lab order integration with OpenMRS. Our standalone mediator service will borrow these patterns.

---

## What This Module Does

`openmrs-module-labonfhir` is an OpenMRS module (OMOD) that runs *inside* OpenMRS. It performs the same logical role as our mediator:
- Detects new lab orders via OpenMRS internal events
- Constructs FHIR Task + ServiceRequest + Patient bundles
- Pushes them to a FHIR server (e.g., OEG2's `external-fhir-api`)
- Polls the FHIR server for completed Tasks and imports results back

**Why not deploy it for Bahmni?**
1. It runs inside OpenMRS — a standalone service is architecturally cleaner and easier to operate
2. It assumes vanilla OpenMRS Order structure; Bahmni uses `BahmniOrder` with extensions that won't map cleanly without modification
3. It detects orders via internal Spring events — those are not accessible to an external process

---

## Patterns to Borrow for the Mediator

### 1. FHIR Transaction Bundle Structure

`LabCreationListener.createLabBundle()` constructs a `Bundle.BundleType.TRANSACTION` with PUT entries:

```
Transaction Bundle
├── Task (status=REQUESTED, intent=ORDER)
│   ├── basedOn → ServiceRequest/{order.uuid}
│   ├── for → Patient/{patient.uuid}
│   ├── owner → Practitioner/{lis.user.uuid}   ← LIS service user
│   ├── encounter → Encounter/{encounter.uuid}
│   ├── location → Location/{location.uuid}     ← optional
│   └── requester → Practitioner/{orderer.uuid} ← optional
├── ServiceRequest/{order.uuid}
├── Patient/{patient.uuid}
├── Encounter/{encounter.uuid}
└── Practitioner resources
```

**Key reference:** `api/src/.../event/LabCreationListener.java` lines 88–125

### 2. Result Polling Scheduler

`FetchTaskUpdates` (OpenMRS scheduled task) shows the result polling loop:
1. Query FHIR server for Tasks with `status=COMPLETED|ACCEPTED|REJECTED|CANCELLED` and `owner=LIS user` and `_lastUpdated >= lastRequestTime`
2. For each completed Task: fetch DiagnosticReport from `Task.output`, parse Observations
3. Create OpenMRS observations + update Order `fulfillerStatus`

Status mapping:
- `COMPLETED` → `Order.FulfillerStatus.COMPLETED` + import results
- `ACCEPTED` → `Order.FulfillerStatus.IN_PROGRESS`
- `REJECTED` / `CANCELLED` → `Order.FulfillerStatus.EXCEPTION`

**Key reference:** `api/src/.../scheduler/FetchTaskUpdates.java`

### 3. FHIR Client Configuration Pattern

`FhirConfig.java` shows how to configure a HAPI FHIR client with Basic Auth or SSL mutual auth. The mediator should replicate this approach:
- `labonfhir.lisUrl` → mediator config: `oeg2.fhir.url`
- `labonfhir.authType` (BASIC or SSL) → mediator config
- Connection pooling via Apache HttpClient

**Key reference:** `api/src/.../FhirConfig.java`

### 4. Retry Queue Pattern

`RetryFailedTasks` shows how to handle failed pushes — persist failed Tasks to a DB table and retry on a schedule.

**Key reference:** `api/src/.../scheduler/RetryFailedTasks.java`

### 5. Event Detection (Not Reused — Internal Only)

The module uses `OrderCreationListener` / `EncounterCreationListener` which subscribe to OpenMRS internal Spring events. **This cannot be used by an external mediator.** The mediator uses REST polling instead. Documented here only to explain why this module cannot be used as-is.

---

## Mediator Implementation Checklist (derived from labonfhir patterns)

- [ ] Borrow bundle construction logic from `LabCreationListener.createLabBundle()`
- [ ] Borrow polling logic from `FetchTaskUpdates.execute()`
- [ ] Borrow FHIR client setup from `FhirConfig.java`
- [ ] Borrow retry queue pattern from `RetryFailedTasks`
- [ ] Replace: internal event detection → REST polling with timestamp cursor
- [ ] Replace: OpenMRS module scheduler → Spring Boot `@Scheduled` tasks
- [ ] Replace: OpenMRS service calls → HTTP calls to `/ws/fhir2/` and `/bahmnicore/` APIs

---

## Key Files (reference only)

| File | Pattern |
|---|---|
| `api/src/.../event/LabCreationListener.java` | Bundle construction |
| `api/src/.../api/LabOrderHandler.java` | Order → FHIR mapping |
| `api/src/.../scheduler/FetchTaskUpdates.java` | Result polling loop |
| `api/src/.../scheduler/RetryFailedTasks.java` | Retry queue |
| `api/src/.../FhirConfig.java` | FHIR client setup |
| `api/src/.../LabOnFhirConfig.java` | Configuration properties |
| `omod/src/main/resources/config.xml` | Module config (for reference on global properties) |
