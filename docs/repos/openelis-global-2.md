# Repository: OpenELIS-Global-2

*Back to [Integration Plan](../../bahmni-openelis-global2-integration-plan.md)*

**Repo:** `I-TECH-UW/OpenELIS-Global-2`
**Local path:** `/Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/OpenELIS-Global-2`
**Deep dive date:** 2026-02-19
**Changes required:** None — configuration only

---

## Role in Integration

OEG2 is the target LIS. It ships with a HAPI FHIR server (`external-fhir-api`) as a standard container. The mediator pushes FHIR bundles to this server; OEG2 polls it for new orders and pushes results back to it. No code changes to OEG2 are needed.

---

## Verified Findings

### `external-fhir-api`
- HAPI FHIR JPA Server **v6.6.0** (Tomcat-based)
- Defined in `docker-compose.yml` as image `itechuw/openelis-global-2-fhir:develop`
- Dev builds use `hapiproject/hapi:v6.6.0-tomcat` directly
- Dockerfile: `fhir/Dockerfile`
- Config: `fhir/hapi_application.yaml`
- Ports: `8081:8080` (HTTP), `8444:8443` (HTTPS)
- Shares PostgreSQL with OEG2 webapp (`clinlims` schema)
- Server base URL: `http://fhir.openelis.org:8080/fhir/` (configurable via `FHIR_SERVER_ADRESS`)

### Order Polling
- **Class:** `FhirApiWorkFlowServiceImpl`
- **Method:** `pollForRemoteTasks()`
- **Interval:** 120 seconds default — `@Scheduled(initialDelay=10000, fixedRateString="${org.openelisglobal.remote.poll.frequency:120000}")`
- **Query:** Tasks with `status=REQUESTED` AND `owner` matches facility identifier
- **Resources fetched per Task:** Task, ServiceRequest, Patient, Specimen, Practitioner

### Task Lifecycle
```
REQUESTED → ACCEPTED (or REJECTED) → RECEIVED → IN_PROGRESS → COMPLETED
```
- OEG2 updates Task status back to `external-fhir-api` at each step
- `ACCEPTED` = at least one test order was accepted; `REJECTED` = none accepted

### Required FHIR Bundle Fields
The mediator must provide a Transaction Bundle containing:

**Task (required fields):**
| Field | Value |
|---|---|
| `status` | `REQUESTED` |
| `intent` | `ORDER` |
| `basedOn` | Reference to ServiceRequest |
| `for` | Reference to Patient |
| `owner` | Reference to Organization matching facility identifier in OEG2 config |

**ServiceRequest (required):**
| Field | Value |
|---|---|
| `status` | any |
| `subject` | Reference to Patient |
| `code` | CodeableConcept with LOINC code |

**Patient (required fields — validated by `TaskInterpreterImpl`):**
| Field | Required | Notes |
|---|---|---|
| `identifier` | Yes | At least one (e.g. MRN) |
| `name` | Yes | At least family name |
| `birthDate` | Yes | Validated — order rejected if missing |
| `gender` | Yes | Validated — order rejected if missing |

### Result Push
- OEG2 creates DiagnosticReport + Observations when results are finalized
- Persists them to `external-fhir-api` via `FhirTransformServiceImpl.transformPersistObjectsUnderSamples()`
- DiagnosticReport status mapping: `Finalized` → `FINAL`, `TechnicalAcceptance` → `PRELIMINARY`, `TechnicalRejected` → `PARTIAL`
- Mediator polls `external-fhir-api` for Tasks with `status=COMPLETED` to detect finished results

### Docker Compose Container Set
```
certs                   # SSL cert generation
db.openelis.org         # PostgreSQL (shared by webapp + FHIR)
oe.openelis.org         # OEG2 webapp (Tomcat)
fhir.openelis.org       # external-fhir-api (HAPI FHIR)
frontend.openelis.org   # React frontend
proxy                   # Nginx reverse proxy
```

---

## Changes Required

**None.** OEG2 is used as-is. Only configuration changes needed.

### Configuration (`volume/properties/common.properties`)

```properties
# OEG2's own FHIR store — leave as default
org.openelisglobal.fhirstore.uri=https://fhir.openelis.org:8443/fhir/

# The mediator's FHIR endpoint (mediator exposes external-fhir-api, or mediator IS the push point)
# In the simplified architecture, point directly at external-fhir-api
org.openelisglobal.remote.source.uri=http://external-fhir-api:8080/fhir/

# Polling interval (ms) — tune as needed
org.openelisglobal.remote.poll.frequency=120000

# Must match the owner reference in Tasks the mediator creates
org.openelisglobal.remote.source.identifier=Practitioner/*

# Push status updates back to mediator/FHIR store
org.openelisglobal.remote.source.updateStatus=true

# Allow HTTP (set false in production, use HTTPS)
org.openelisglobal.fhir.subscriber.allowHTTP=true
```

---

## Open Questions (repo-specific)

| # | Question |
|---|---|
| 1 | What value should `org.openelisglobal.remote.source.identifier` be set to? It must match the `Task.owner` reference the mediator sets. Need to agree on the identifier scheme. |
| 2 | In the simplified architecture, OEG2 polls `external-fhir-api` directly. Does `org.openelisglobal.remote.source.uri` point at `external-fhir-api` (same server), or at a separate mediator endpoint? Needs to be confirmed during Phase 1a. |
| 3 | LOINC codes required for `ServiceRequest.code`. OEG2 rejects orders without them. Phase 2 (test catalog) must resolve this before Phase 3 mediator build. |

---

## Key Files

| File | Purpose |
|---|---|
| `docker-compose.yml` | Production container definitions |
| `dev.docker-compose.yml` | Dev setup with local builds |
| `fhir/Dockerfile` | HAPI FHIR container build |
| `fhir/hapi_application.yaml` | HAPI FHIR configuration |
| `volume/properties/common.properties` | OEG2 runtime configuration (incl. FHIR endpoints) |
| `src/main/java/org/openelisglobal/dataexchange/fhir/service/FhirApiWorkFlowServiceImpl.java` | Core polling + Task lifecycle logic |
| `src/main/java/org/openelisglobal/dataexchange/fhir/service/TaskInterpreterImpl.java` | Validates incoming FHIR bundle fields |
| `src/main/java/org/openelisglobal/dataexchange/fhir/service/FhirTransformServiceImpl.java` | Creates DiagnosticReport + Observations from results |
