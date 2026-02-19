# Repository: bahmni-core

*Back to [Integration Plan](../../bahmni-openelis-global2-integration-plan.md)*

**Repo:** `Bahmni/bahmni-core`
**Local path:** `/Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/bahmni-core`
**Deep dive date:** 2026-02-19
**Changes required:** Possibly minor — verify timestamp filtering on orders API

---

## Role in Integration

Bahmni-core is the OpenMRS module that powers Bahmni's clinical APIs. The mediator calls it to:
1. **Detect new lab orders** — poll for orders created since last check
2. **Fetch order and patient data** — to construct FHIR bundles
3. **Receive results back** — via the fhir2 module writing observations, which bahmni-core then serves

---

## Verified Findings

### AtomFeed — Both Directions

The lab order flow diagram confirms OpenMRS publishes encounter/order events outbound via AtomFeed. Two distinct flows exist:

| Direction | Publisher | Consumer | Feed |
|---|---|---|---|
| **Outbound** (orders) | OpenMRS via `openmrs-module-atomfeed` | Old OpenELIS, PACS integration, new mediator | `openmrs/ws/atomfeed/encounter/recent` |
| **Inbound** (results) | Old OpenELIS | bahmni-core's `openmrs-elis-atomfeed-client-omod` | OpenELIS's own feed |

The new mediator subscribes to the outbound encounter feed — the same feed the old OpenELIS and pacs-integration consume. See Decision 7 in [Decisions Log](../decisions-log.md).

**bahmni-core's own Spring events** (`BAHMNI_ENCOUNTER_CREATED` etc.) are in-process only — a standalone mediator cannot use those.

### Order Creation Flow
```
POST /rest/v1/bahmnicore/bahmniencounter
  ↓ BahmniEncounterController
  ↓ BahmniEncounterTransactionService.save()
  ↓ EmrEncounterService.save()
  ↓ OpenMRS DB: encounter + orders tables
  ↓ (Spring event fired in-process — not visible externally)
```

### REST APIs Available to Mediator

**Query lab orders:**
```
GET /rest/v1/bahmnicore/orders
  ?patientUuid=<UUID>         [required]
  ?orderTypeUuid=<UUID>       [optional]
  ?numberOfVisits=<int>       [optional]
  ?visitUuid=<UUID>           [optional]
```
Response: `List<BahmniOrder>` — includes `orderUuid`, `conceptUuid`, `provider`, `orderDate`, `fulfillerStatus`

**Fetch patient profile:**
```
GET /rest/v1/bahmnicore/patientprofile/<patientUuid>
```
Response: full patient with identifiers, names, gender, birthDate, attributes

**Fetch encounter:**
```
GET /rest/v1/bahmnicore/bahmniencounter/<encounterUuid>
```

**Fetch lab results (read path — used by Bahmni UI):**
```
GET /rest/v1/bahmnicore/labOrderResults?patientUuid=<UUID>
```
This is the endpoint the UI calls. Results must land in OpenMRS as observations for this to work.

### FHIR Support
None in bahmni-core. All APIs are custom REST/JSON. The mediator uses `openmrs-module-fhir2` (separate module) to push results back as FHIR resources.

### Existing OpenELIS Integration Pattern
`openmrs-elis-atomfeed-client-omod` is a useful reference showing how bahmni-core consumes external events — relevant for understanding the result-import side, even though the new mediator does not use this module.

---

## Changes Required

### No Changes Required

The mediator subscribes to the existing `openmrs-module-atomfeed` encounter feed. No changes to bahmni-core are required. The feed already exists and is consumed by old OpenELIS and pacs-integration today.

### No Other Changes Expected
- Order creation flow: unchanged
- Patient API: unchanged
- Result display: unchanged as long as observations are written correctly

---

## Open Questions (repo-specific)

| # | Question |
|---|---|
| 1 | Does `fulfillerStatus` on `BahmniOrder` get updated automatically when the mediator writes observations back via fhir2? Or does the mediator need to explicitly PATCH the order's `fulfillerStatus`? |
| 2 | Does `fulfillerStatus` on `BahmniOrder` get updated when the mediator writes observations back via fhir2? Or does the mediator need to explicitly PATCH the order's `fulfillerStatus`? |
| 3 | The result display endpoint `/bahmnicore/labOrderResults` — does it read from OpenMRS obs directly? If the mediator writes results via `/ws/fhir2/DiagnosticReport` or `/ws/fhir2/Observation`, will they show up here automatically? |

---

## Key Files

| File | Purpose |
|---|---|
| `bahmnicore-omod/src/.../web/v1_0/controller/BahmniEncounterController.java` | POST encounter (order creation entry point) |
| `bahmnicore-omod/src/.../web/v1_0/controller/BahmniOrderController.java` | GET orders (mediator polling endpoint) |
| `bahmnicore-omod/src/.../web/v1_0/controller/display/controls/BahmniLabOrderResultController.java` | GET lab results (UI display endpoint) |
| `bahmnicore-api/src/.../events/` | Spring event infrastructure (in-process only) |
| `openmrs-elis-atomfeed-client-omod/` | Reference: how inbound OpenELIS events are consumed |
