# Proposed Flow Detail

*Back to [Integration Plan](../bahmni-openelis-global2-integration-plan.md)*

---

A custom **mediator service** (standalone microservice) detects lab order events in OpenMRS, creates FHIR bundles, and pushes them to OE-Global-2's FHIR store (`external-fhir-api`). OE-Global-2 polls that same store. Results flow back through the mediator.

## Sequence Diagram

```mermaid
sequenceDiagram
    participant UI as Bahmni UI
    participant MRS as OpenMRS
    participant MED as Mediator Service<br/>(standalone container)
    participant FHIR as external-fhir-api<br/>(OE-Global-2's FHIR store)
    participant OEG as OE-Global-2
    participant LAB as Lab Technician

    Note over UI,LAB: Order Creation
    UI->>MRS: Doctor saves consultation with lab orders
    MRS->>MRS: Publishes order event
    MED-->>MRS: Detects new order event
    MED->>MRS: Fetches order + patient data (REST/FHIR)
    MED->>MED: Creates FHIR Task + ServiceRequest + Patient
    MED->>FHIR: Pushes FHIR bundle

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
    MED-->>FHIR: Polls for completed Tasks
    MED->>MRS: Pushes results to OpenMRS (REST/FHIR)
    MRS->>MRS: Creates lab result encounter + observations

    Note over UI,LAB: View Results (unchanged)
    UI->>MRS: Clinician opens patient dashboard
    MRS-->>UI: Displays lab results
```

## Components

| Component | Role |
|---|---|
| **Custom mediator service** (new, standalone container) | Active orchestrator — detects lab order events in OpenMRS, creates FHIR bundles, pushes to FHIR store, polls for results, pushes results back to OpenMRS |
| [`openmrs-module-fhir2`](https://github.com/openmrs/openmrs-module-fhir2) | Passive API layer — translates OpenMRS data to/from FHIR format |
| [OpenELIS-Global-2](https://github.com/DIGI-UW/OpenELIS-Global-2) | Lab system — polls for orders, processes them, pushes results back |
| `external-fhir-api` | OE-Global-2's HAPI FHIR store — doubles as the shared FHIR store |

| Direction | Mechanism | Latency |
|---|---|---|
| Orders out (OpenMRS → FHIR Store) | Mediator detects event → pushes FHIR bundle | Seconds |
| Results back (FHIR Store → OpenMRS) | Mediator polls for completed Tasks | Configurable (seconds-minutes) |
| Patient sync (OpenMRS → FHIR Store) | Mediator detects patient creation → pushes Patient resource | Seconds |

For deeper technical details on LOINC matching, Task lifecycle, and mediator service responsibilities, see [Technical Reference](technical-reference.md).

*Fallback architecture (Full OpenHIE with OpenHIM + SHR) is documented in [fallback-option-a.md](fallback-option-a.md).*
