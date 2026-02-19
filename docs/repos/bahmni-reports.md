# Repository Analysis: bahmni-reports

*Back to [Integration Plan](../../bahmni-openelis-global2-integration-plan.md)*

**Analysed:** 2026-02-19 | **Repo:** bahmni-reports

---

## Role in the Integration

`bahmni-reports` is Bahmni's server-side reporting engine. It serves report requests from the Bahmni UI, executes SQL against one of five configured datasources, and renders output as PDF or Excel via DynamicReports/Jasper.

**Impact:** Two report types (`TestCount` and `ElisGeneric`) query OpenELIS PostgreSQL directly. They will break with OEG2. Five lab report types already query OpenMRS and are unaffected.

---

## Architecture

### Datasource Configuration

Five C3P0 connection-pooled datasources, configured in `BahmniReportsConfiguration.java` and `bahmni-reports.properties.template`:

| Datasource key | DB type | Connection target |
|---|---|---|
| `openelis` | PostgreSQL | `clinlims` DB on `OPENELIS_DB_SERVER` |
| `openmrs` | MySQL | OpenMRS DB |
| `bahmniReports` | MySQL | Reports DB |
| `bahmniMart` | PostgreSQL | Mart DB |
| `openerp` | PostgreSQL | Odoo DB |

**Datasource selection:** Each report template class is annotated `@UsingDatasource("openelis")` / `@UsingDatasource("openmrs")` etc. The `AllDatasources` component routes the connection at runtime. No cross-database joins — every query is single-source.

### Report Flow

1. HTTP request → `MainReportController` → `ReportGenerator.invoke()`
2. Load report definition JSON from external config (`reports.json` served by `bahmni-web`)
3. Deserialise report type → instantiate `Report` class (45 registered types via Jackson `@JsonSubTypes`)
4. Get `BaseReportTemplate` → select datasource via annotation
5. Execute SQL → build Jasper report → return PDF/Excel

---

## OpenELIS-Dependent Report Types

### 1. `TestCount`

| Attribute | Value |
|---|---|
| Class | `TestCountReport.java` |
| Template | `TestCountTemplate.java` (`@UsingDatasource("openelis")`) |
| SQL file | `src/main/resources/sql/testCount.sql` |
| Output | Count of results per department and test, filtered by date range |

**clinlims tables queried:**

| Table | Role |
|---|---|
| `test_section` | Test department (Haematology, Microbiology, etc.) |
| `test` | Test definitions, linked to `test_section` via `test_section_id` |
| `analysis` | Per-sample test analyses, linked to `test` via `test_id` |
| `result` | Individual results, linked to `analysis` via `analysis_id`; date-filtered on `lastupdated` |
| `test_result` | Result type metadata (discriminated vs numeric) |

**Report columns produced:** Department, Test Name, Total Count, Positive Count, Negative Count — all date-range filtered.

**Migration:** This report **cannot** be repointed to OpenMRS as-is. OpenMRS has no `test_section` or `analysis` objects — results are `obs` records grouped by concept. The report must be rewritten. Options:
- (a) Rewrite against OEG2's PostgreSQL schema directly (adds OEG2 as a 6th datasource — non-standard `@UsingDatasource` key)
- (b) Rewrite against OpenMRS observations (the mediator already pushes results into OpenMRS `obs`; a concept-based grouping can approximate the department/test breakdown)
- Option (b) is preferred: keeps all lab data in a single authoritative source and avoids a new direct DB dependency on OEG2

### 2. `ElisGeneric`

| Attribute | Value |
|---|---|
| Class | `ElisSqlReport.java` |
| Template | `ElisSqlReportTemplate.java` (`@UsingDatasource("openelis")`) |
| SQL file | User-defined (configured per deployment in `reports.json`) |
| Output | Arbitrary tabular query against clinlims |

**Migration:** Any `ElisGeneric` report must have its SQL rewritten against OEG2's schema or an OpenMRS equivalent. These are deployment-specific — each installation must audit its own `ElisGeneric` report definitions.

---

## Lab Report Types Already Using OpenMRS (Unaffected)

These five report types query OpenMRS `obs` and `orders` tables. Because the mediator writes results back to OpenMRS, they will continue to work correctly after OEG2 migration:

| Report Type | Class | SQL file | Data |
|---|---|---|---|
| `GenericLabOrderReport` | `GenericLabOrderReport.java` | `genericLabOrderReport.sql` | Lab orders + obs from OpenMRS |
| `PatientsWithLabtestResults` | `PatientsWithLabtestResultsReport.java` | `patientsWithLabtestResults.sql` | Patients with lab results in OpenMRS `obs` |
| `GenericObservationReport` | `GenericObservationReport.java` | `genericObservationReport.sql` | OpenMRS observations |
| `GenericObservationFormReport` | `GenericObservationFormReport.java` | `genericObservationFormReport.sql` | OpenMRS form-based observations |
| `OrderFulfillmentReport` | `OrderFulfillmentReport.java` | `orderFulfillmentReport.sql` | Order fulfilment status via OpenMRS orders + obs |

All five use `@UsingDatasource("openmrs")`.

OpenMRS lab result observation structure the mediator must preserve:
- Concepts: `LAB_ABNORMAL`, `LAB_MINNORMAL`, `LAB_MAXNORMAL`, `LAB_NOTES`, `LAB_REPORT`, `REFERRED_OUT`
- Standard `obs` fields: `concept_id`, `value_numeric`, `value_text`, `obs_datetime`

---

## Changes Required

| Item | Scope | Phase |
|---|---|---|
| Rewrite `TestCount` report against OpenMRS observations | Medium — new SQL and possibly new template class | Phase 4 |
| Audit and rewrite deployment-specific `ElisGeneric` reports | Deployment-specific; may be none | Phase 4 |
| No changes to the 5 OpenMRS-based lab reports | None | — |
| No changes to datasource configuration (OpenELIS datasource simply goes unused) | None | — |

---

## Key Files

| File | Purpose |
|---|---|
| `src/main/java/org/bahmni/reports/BahmniReportsConfiguration.java` | Datasource beans (lines 73–143) |
| `package/docker/bahmni-reports/template/bahmni-reports.properties.template` | Docker env-var → property mapping |
| `src/main/java/org/bahmni/reports/model/AllDatasources.java` | Datasource routing via `@UsingDatasource` |
| `src/main/java/org/bahmni/reports/model/Report.java` | 45 report type registrations |
| `src/main/java/org/bahmni/reports/report/TestCountReport.java` | TestCount report class |
| `src/main/java/org/bahmni/reports/template/TestCountTemplate.java` | TestCount template (`@UsingDatasource("openelis")`) |
| `src/main/resources/sql/testCount.sql` | The only SQL file querying clinlims |
| `src/main/java/org/bahmni/reports/report/ElisSqlReport.java` | ElisGeneric report class |
| `src/main/java/org/bahmni/reports/template/ElisSqlReportTemplate.java` | ElisGeneric template (`@UsingDatasource("openelis")`) |
| `src/main/java/org/bahmni/reports/web/ReportGenerator.java` | Report execution engine |
