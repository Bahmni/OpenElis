# Current Flow Detail: AtomFeed-based Integration

*Back to [Integration Plan](../bahmni-openelis-global2-integration-plan.md)*

---

## Sequence Diagram

```mermaid
sequenceDiagram
    participant UI as Bahmni UI<br/>(bahmniapps)
    participant MRS as OpenMRS<br/>(bahmni-core + atomfeed)
    participant ELIS as OpenELIS<br/>(Bahmni fork)
    participant LAB as Lab Technician

    Note over UI,LAB: Step 1-2: Order Creation
    UI->>MRS: Doctor saves consultation with lab orders
    MRS->>MRS: Creates encounter with ServiceRequest
    MRS->>MRS: Atomfeed module publishes encounter event

    Note over UI,LAB: Step 3: Order Pickup
    ELIS-->>MRS: Polls encounter feed (every 5 sec)
    MRS-->>ELIS: Returns new encounter event
    ELIS->>MRS: Fetches encounter details via REST
    MRS-->>ELIS: Returns patient info + test orders
    ELIS->>ELIS: Creates patient (if new) + lab accession/sample

    Note over UI,LAB: Step 4: Lab Work
    LAB->>ELIS: Performs sample collection
    LAB->>ELIS: Conducts tests, enters results
    LAB->>ELIS: Validates results (supervisor review)
    LAB->>ELIS: Marks as finalized

    Note over UI,LAB: Step 5-7: Result Return
    ELIS->>ELIS: Publishes result to patient atomfeed
    MRS-->>ELIS: Polls patient feed (every 15 sec)
    ELIS-->>MRS: Returns accession/result event
    MRS->>ELIS: Fetches result details via REST
    ELIS-->>MRS: Returns test results
    MRS->>MRS: Creates lab result encounter + observations
    MRS->>MRS: Creates FHIR DiagnosticReport, updates order status

    Note over UI,LAB: Step 8: View Results
    UI->>MRS: Clinician opens patient dashboard
    MRS-->>UI: Displays lab results with values, units, ranges
```

## Step-by-Step Detail

| Step | System | What Happens | Repository |
|---|---|---|---|
| **1. Create order** | Bahmni UI | Doctor opens consultation → Orders tab → selects lab tests → saves | `openmrs-module-bahmniapps` |
| **2. Publish event** | OpenMRS | Creates encounter with orders; atomfeed publishes encounter event | `openmrs/openmrs-module-atomfeed`, `bahmni-core` |
| **3. Poll & fetch** | OpenELIS | Polls encounter feed (5s interval); fetches encounter via REST; creates patient + accession | `Bahmni/OpenElis` |
| **4. Lab work** | OpenELIS | Sample collection → testing → result entry → validation → finalization | `Bahmni/OpenElis` |
| **5. Publish results** | OpenELIS | Publishes atomfeed event with accession UUID | `Bahmni/OpenElis` |
| **6. Poll & fetch** | OpenMRS | Polls OpenELIS patient feed (15s interval); fetches result details via REST | `bahmni-core (openmrs-elis-atomfeed-client-omod)` |
| **7. Process results** | OpenMRS | Creates lab result encounter, observations, FHIR DiagnosticReport; updates order status | `elis-fhir-result-support`, `bahmni-module-fhir2-addl-extension` |
| **8. View results** | Bahmni UI | Clinician sees results on patient dashboard with values, units, reference ranges | `openmrs-module-bahmniapps` |

## Integration Feeds

| Feed | Direction | URL | Poll Interval |
|---|---|---|---|
| Encounter feed | OpenMRS → OpenELIS | `http://openmrs:8080/openmrs/ws/atomfeed/encounter/recent` | 5 seconds |
| Patient result feed | OpenELIS → OpenMRS | `http://openelis:8052/openelis/ws/feed/patient/recent` | 15 seconds |
