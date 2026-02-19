# Repository: openmrs-module-bahmniapps

*Back to [Integration Plan](../../bahmni-openelis-global2-integration-plan.md)*

**Repo:** `Bahmni/openmrs-module-bahmniapps`
**Local path:** `/Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/openmrs-module-bahmniapps`
**Deep dive date:** 2026-02-19
**Changes required:** None — confirmed

---

## Role in Integration

This is the **Angular-based Bahmni clinical web application** used by clinicians for lab order entry and result viewing. It is distinct from `bahmni-frontend` (newer React micro-frontend, which has no lab UI yet).

---

## Key Finding: UI Is Fully Decoupled From OpenELIS

**The Angular UI makes zero direct calls to OpenELIS.** All API calls go through OpenMRS/bahmni-core endpoints. The lab UI was designed to be LIS-agnostic from the start. Switching to OEG2 requires **no changes to this codebase**.

---

## Verified Findings

### 1. No Hardcoded OpenELIS URLs

Search for "openelis", "8052", "clinlims", "lims" in JS/HTML files: **none found**.

The UI has no direct network calls to OpenELIS.

### 2. No "Open in Lab" Links

No `window.open` calls pointing to an external lab URL. The only `$window.open` calls in the clinical module are for:
- Opening a consultation board in a new tab
- Print functionality

No "View in OpenELIS", "Open in lab", or equivalent links exist anywhere in the codebase.

### 3. Lab Order Entry Flow

**Endpoint:** `POST /openmrs/ws/rest/v1/bahmnicore/bahmniencounter`

Flow: Doctor selects tests → saves consultation → `consultationController.js:save()` → `encounterService.create()` → posts encounter with `orders` array to bahmni-core → OpenMRS publishes AtomFeed encounter event → mediator picks up.

No changes needed. The encounter POST format is independent of the downstream LIS.

### 4. Test Catalog

**Source:** OpenMRS concept sets (NOT OpenELIS)

The UI fetches test definitions via `conceptSetService.getConcept()` using the concept names:
- `"Lab Samples"` (labConceptSetName)
- `"Lab Departments"` (labDepartmentsConceptSetName)

**Endpoint:** `GET /openmrs/ws/rest/v1/concept?s=byFullySpecifiedName`

Hierarchy: Lab Samples → Sample Types → Panels → Tests. Each test object carries: `uuid`, `name`, `sample`, `orderTypeName`. No OpenELIS involvement.

### 5. Lab Results Display

**Endpoint:** `GET /openmrs/ws/rest/v1/bahmnicore/labOrderResults`

This is a bahmni-core endpoint that aggregates observations from OpenMRS and returns a structured response. The UI reads the following fields:

| Field | Type | Display |
|---|---|---|
| `accessionUuid` | String | Used for grouping only — NOT shown to clinician |
| `accessionDateTime` | String (ISO 8601) | Shown as accession date/time header |
| `accessionNotes` | `[{text, providerName}]` | Shown as notes |
| `testName` | String | Test name |
| `panelName` | String | Panel grouping header |
| `result` | String | Result value |
| `resultDateTime` | String | Timestamp of result |
| `minNormal` | Number/String | Shown as "(minNormal - maxNormal)" |
| `maxNormal` | Number/String | Shown alongside minNormal |
| `testUnitOfMeasurement` | String | Units shown next to range |
| `abnormal` | Boolean | CSS class `is-abnormal` (red highlight) |
| `referredOut` | Boolean | Shows "R" badge |
| `uploadedFileName` | String | Paperclip icon → links to `/uploaded_results/{filename}` |
| `notes` | String | Provider comments |
| `provider` | String | Provider name in notes footer |

Request parameters: `patientUuid`, `visitUuids[]`, `numberOfVisits`, `initialAccessionCount`, `latestAccessionCount`, `sortResultColumnsLatestFirst`, `groupOrdersByPanel`.

The response also includes `tabularResult` (with `orders`, `dates`, `values` arrays) for the chart/tabular view.

### 6. No Order Status Display

The UI shows **no order fulfillment status** (pending / accepted / in-progress / completed). There are no references to `fulfillerStatus`, `ORDER_STATUS`, or equivalent fields anywhere in the lab results display code.

Orders either appear as "result available" or not at all. This means order status tracking (Task lifecycle REQUESTED → ACCEPTED → IN_PROGRESS → COMPLETED) is invisible to the Bahmni UI — no UI work needed to display it, but also no UI feedback for in-flight orders.

### 7. Lab Report Upload Support

Uploaded lab reports (PDF/image) are supported. Files are served from `/uploaded_results/{filename}`. The `uploadedFileName` field in the result drives this. As long as the mediator correctly passes this field through from OEG2 results, the UI will display the paperclip link.

The file serving endpoint (`/uploaded_results/`) is a bahmni-core concern, not this UI.

### 8. Referred Out

`referredOut: true` on a test result shows an "R" badge. This is a boolean flag — no details about which external lab. As long as the mediator maps OEG2's referred-out concept correctly when writing observations back to OpenMRS, the UI will display it.

---

## Files of Interest

| File | Purpose |
|---|---|
| `ui/app/clinical/consultation/controllers/consultationController.js` | Order submission (save consultation) |
| `ui/app/clinical/consultation/services/labTestsProvider.js` | Fetch test catalog from OpenMRS |
| `ui/app/clinical/consultation/mappers/labConceptsMapper.js` | Map concept hierarchy to test objects |
| `ui/app/clinical/displaycontrols/investigationresults/services/labOrderResultService.js` | Fetch and transform lab results |
| `ui/app/clinical/displaycontrols/investigationresults/directives/investigationTableRow.js` | Result row rendering logic |
| `ui/app/clinical/displaycontrols/investigationresults/views/investigationTableRow.html` | Result row template |
| `ui/app/clinical/displaycontrols/investigationresults/views/investigationChart.html` | Tabular/chart view template |
| `ui/app/common/constants.js:103` | `bahmniLabOrderResultsUrl` = `/openmrs/ws/rest/v1/bahmnicore/labOrderResults` |
| `ui/app/common/constants.js:113` | `labResultUploadedFileNameUrl` = `/uploaded_results/` |

---

## Changes Required

**None.** The Angular UI is fully decoupled from the LIS. It only talks to OpenMRS/bahmni-core endpoints. As long as:
1. The mediator correctly writes DiagnosticReport + Observations back to OpenMRS
2. bahmni-core's `labOrderResults` endpoint populates all expected fields from those observations

…the UI will display OEG2 results identically to current OpenELIS results. The critical integration point is in **bahmni-core** (the `labOrderResults` endpoint implementation), not in this UI.

---

## Open Question (Moved to Integration Plan)

**Open Question 6 (new):** Does `bahmni-core`'s `labOrderResults` endpoint correctly populate `minNormal`, `maxNormal`, `testUnitOfMeasurement`, `referredOut`, and `uploadedFileName` from OpenMRS observations written by the mediator? Specifically: does it read reference ranges from the observation itself or from the OpenMRS concept numeric limits? If from concept limits, OEG2-specific result ranges may not appear. See the main integration plan for tracking.
