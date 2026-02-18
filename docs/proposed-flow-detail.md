# Proposed Flow Detail

*Back to [Integration Plan](../bahmni-openelis-global2-integration-plan.md)*

---

Two architecture options are proposed. The data flow is similar — the difference is what sits between OpenMRS and OE-Global-2. See [Architecture Decision](../bahmni-openelis-global2-integration-plan.md#5-architecture-decision-full-openhie-vs-simplified) for the comparison and recommendation.

## Option B: Simplified Flow (recommended)

OpenMRS pushes orders directly to OE-Global-2's own FHIR store (`external-fhir-api`). OE-Global-2 polls that same store. Results flow back through the same store.

```mermaid
sequenceDiagram
    participant UI as Bahmni UI
    participant MRS as OpenMRS<br/>(FHIR2 + Lab on FHIR)
    participant FHIR as external-fhir-api<br/>(OE-Global-2's FHIR store)
    participant OEG as OE-Global-2
    participant LAB as Lab Technician

    Note over UI,LAB: Order Creation
    UI->>MRS: Doctor saves consultation with lab orders
    MRS->>MRS: Lab on FHIR creates FHIR Task + ServiceRequest + Patient
    MRS->>FHIR: Pushes FHIR bundle

    Note over UI,LAB: Order Pickup
    OEG-->>FHIR: Polls for Tasks (status: REQUESTED)
    FHIR-->>OEG: Returns Task + ServiceRequest + Patient
    OEG->>OEG: Matches LOINC → test/panel, creates order + sample
    OEG->>FHIR: Updates Task (status: ACCEPTED)

    Note over UI,LAB: Lab Work (unchanged)
    LAB->>OEG: Sample collection → testing → validation → finalization

    Note over UI,LAB: Result Return
    OEG->>OEG: Creates DiagnosticReport + Observations
    OEG->>FHIR: Pushes results + Task (status: COMPLETED)
    MRS-->>FHIR: Lab on FHIR polls for completed Tasks
    MRS->>MRS: Imports results, updates order status

    Note over UI,LAB: View Results (unchanged)
    UI->>MRS: Clinician opens patient dashboard
    MRS-->>UI: Displays lab results
```

## Option A: Full OpenHIE Flow (reference implementation)

OpenMRS and OE-Global-2 communicate through a separate Shared Health Record (SHR), with OpenHIM as an API gateway handling routing and auth.

```mermaid
sequenceDiagram
    participant UI as Bahmni UI
    participant MRS as OpenMRS<br/>(FHIR2 + Lab on FHIR)
    participant HIM as OpenHIM<br/>(proxy + auth)
    participant SHR as SHR<br/>(HAPI FHIR)
    participant OEG as OE-Global-2
    participant LAB as Lab Technician

    Note over UI,LAB: Order Creation
    UI->>MRS: Doctor saves consultation with lab orders
    MRS->>MRS: Lab on FHIR creates FHIR Task + ServiceRequest + Patient
    MRS->>HIM: Pushes FHIR bundle (authenticated)
    HIM->>SHR: Routes to Shared Health Record

    Note over UI,LAB: Order Pickup
    OEG-->>HIM: Polls for Tasks (status: REQUESTED, authenticated)
    HIM-->>SHR: Routes query
    SHR-->>OEG: Returns Task + ServiceRequest + Patient
    OEG->>OEG: Matches LOINC → test/panel, creates order + sample
    OEG->>HIM: Updates Task (status: ACCEPTED)
    HIM->>SHR: Stores update

    Note over UI,LAB: Lab Work (unchanged)
    LAB->>OEG: Sample collection → testing → validation → finalization

    Note over UI,LAB: Result Return
    OEG->>OEG: Creates DiagnosticReport + Observations
    OEG->>HIM: Pushes results + Task (status: COMPLETED)
    HIM->>SHR: Stores results
    MRS-->>HIM: Lab on FHIR polls for completed Tasks
    HIM-->>SHR: Routes query
    MRS->>MRS: Imports results, updates order status

    Note over UI,LAB: View Results (unchanged)
    UI->>MRS: Clinician opens patient dashboard
    MRS-->>UI: Displays lab results
```

## Which Module Does What

| Module | Role | Present in |
|---|---|---|
| [`openmrs-module-fhir2`](https://github.com/openmrs/openmrs-module-fhir2) | Passive API layer — translates OpenMRS data to/from FHIR format | Both options |
| [`openmrs-module-labonfhir`](https://github.com/openmrs/openmrs-module-labonfhir) | Active orchestrator — detects lab orders (via JMS), pushes FHIR bundles, polls for results | Both options |
| [OpenELIS-Global-2](https://github.com/DIGI-UW/OpenELIS-Global-2) | Lab system — polls for orders, processes them, pushes results back | Both options |
| `external-fhir-api` | OE-Global-2's HAPI FHIR store — in Option B, doubles as the shared store | Both options (shared store in B) |
| `shr-hapi-fhir` | Separate Shared Health Record (HAPI FHIR) | Option A only |
| OpenHIM (`openhim-core`) | API gateway — routes `/fhir/*` requests, handles auth + audit | Option A only |

| Direction | Mechanism | Latency |
|---|---|---|
| Orders out (OpenMRS → FHIR Store) | JMS event → instant push | Seconds |
| Results back (FHIR Store → OpenMRS) | Scheduled polling (`FetchTaskUpdates`) | Configurable (seconds-minutes) |

For deeper technical details on how Lab on FHIR detects orders, LOINC matching, and Task lifecycle, see [Technical Reference](technical-reference.md).
