# Integration Q&A Detail

*Back to [Integration Plan](../bahmni-openelis-global2-integration-plan.md)*

---

These questions were investigated during the analysis phase and are now answered. They informed the integration plan.

## Q1: How do I send a lab order to OE-Global-2?

**Answer:** The [`openmrs-module-labonfhir`](https://github.com/openmrs/openmrs-module-labonfhir) module (not the FHIR2 module alone) creates FHIR Task + ServiceRequest bundles and pushes them to a shared FHIR store. OE-Global-2 polls that store for new Tasks.

This was confirmed by [Moses Mutesasira from the OE-Global-2 team](https://talk.openelis-global.org/t/integration-with-openmrs-over-fhir/1702/2): the exchange is "purely FHIR based" using "Lab On FHIR module and FHIR2 module" together. The Lab Integration module (IsantePlus-specific) is **not** for general OE-Global-2 integration.

**How it works in the reference implementation:**

1. **OpenMRS side:** Lab on FHIR module detects a new lab order (via JMS event from Hibernate), creates a FHIR transaction bundle (Task + ServiceRequest + Patient), and pushes it to the SHR via OpenHIM
2. **OE-Global-2 side:** Poller (`FhirApiWorkFlowServiceImpl`) queries the SHR (via OpenHIM) for Tasks with status `REQUESTED`

**What Bahmni/OpenMRS must do:**
- Install `openmrs-module-labonfhir` (v1.5.3+) — **this is not currently part of Bahmni's OpenMRS distribution**
- Configure it to push FHIR bundles to the SHR
- Ensure lab orders carry LOINC codes in the ServiceRequest

---

## Q2: How does the EMR know results are ready?

**Answer:** OE-Global-2 pushes results to the **Shared Health Record (SHR)** via OpenHIM. The Task resource tracks status throughout the lifecycle. Lab on FHIR's `FetchTaskUpdates` scheduled task polls the SHR for completed Tasks and imports DiagnosticReport + Observations back into OpenMRS.

Per the [community discussion](https://talk.openelis-global.org/t/integration-with-openmrs-over-fhir/1702/2): the Task serves as "a container Resource to track the status of the Order" and the exchange returns FHIR DiagnosticReport + Observation (not HL7v2).

**How results flow:**
- OE-Global-2 pushes DiagnosticReport + Observations + Task (COMPLETED) to the SHR
- Lab on FHIR's `FetchTaskUpdates` polls the SHR for status changes and imports results

---

## Q3: How does OE-Global-2 know what test to run?

**Answer:** LOINC codes only. No fallback to custom codes in the FHIR path.

When OE-Global-2 receives a FHIR ServiceRequest, the `TaskInterpreter` loops through `code.coding` entries looking for `system = http://loinc.org`, then looks up the LOINC code in its test catalog. If not found, the order is rejected as `UNSUPPORTED_TESTS`.

**Test entity fields:**

| Field | Used for FHIR matching? | Notes |
|---|---|---|
| `loinc` (240 chars) | **Yes — the only field used** | Required for FHIR orders |
| `local_code` (10 chars) | No | Optional, internal use only |
| `description` (60 chars) | No | Human-readable name |

**Method selection at lab execution time:** Per the [community discussion](https://talk.openelis-global.org/t/openelis-global-capability-for-selecting-a-specific-method-for-a-given-order/1691), OE-Global-2 supports selecting the specific method (EIA, PCR, STAIN, CULTURE, etc.) at the time of test execution — not at order time. You don't need separate LOINC codes per method at order time.

**Implication for Bahmni:**
- Every test and panel ordered from Bahmni **must have a LOINC code**
- The same LOINC code must exist in both OpenMRS and OE-Global-2
- If Bahmni currently uses only custom codes, **LOINC mapping is a prerequisite**
- LOINC mapping can be at the **test level** (not method level)

---

## Q4: How do I set up master data?

**Answer:** OE-Global-2 has an admin UI, CSV bulk import on startup, and FHIR-based import for organizations and providers.

| Master Data | Recommended Setup Method | Details |
|---|---|---|
| **Tests + panels** | CSV files on startup | Drop CSV in `/var/lib/openelis-global/configuration/backend/tests/`. Format: `testName,testSection,sampleType,loinc,isActive,...` Auto-loaded, checksum-tracked. |
| **Sample types** | CSV files on startup | `configuration/sampleTypes/*.csv` |
| **Dictionaries** | CSV files on startup | `configuration/dictionaries/*.csv` |
| **Result ranges** | Admin UI or REST API | No CSV import — must be configured per test via UI |
| **Organizations/centers** | FHIR import from OpenMRS | Config: `org.openelisglobal.facilitylist.fhirstore=http://openmrs:8080/openmrs/ws/fhir2/R4`. Runs on schedule. |
| **Users/providers** | FHIR import + local creation | Practitioners auto-imported from OpenMRS FHIR. Lab-specific accounts created locally via admin UI. |
| **Roles** | CSV files on startup | `configuration/roles/*.csv` |

**Recommended approach for Bahmni:** Create a "Bahmni default" CSV configuration set checked into version control. Mount as a Docker volume. Organizations and providers auto-sync from OpenMRS via FHIR.

---

## Q5: How does Lab on FHIR detect new orders? (Technical)

**Answer:** Via OpenMRS's JMS-based event system, not polling.

On module startup, `LabOrderManager.enableLisConnector()` subscribes to Hibernate entity events:

```java
Event.subscribe(Order.class, Event.Action.CREATED.toString(), orderListener);
```

When a clinician saves a lab order, Hibernate persists the `Order`, the OpenMRS Event module publishes a JMS message, and Lab on FHIR's `OrderCreationListener` receives it. The listener:
1. Loads the Order by UUID
2. Checks if it's a `TestOrder`
3. Builds a FHIR Task + ServiceRequest + Patient bundle
4. Pushes it to the configured LIS URL (SHR via OpenHIM)

For the return path, `FetchTaskUpdates` is a scheduled polling task that checks the SHR for completed Tasks.
