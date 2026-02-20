# Repository Deep Dive: bahmni-mart

*Back to [Integration Plan](../../bahmni-openelis-global2-integration-plan.md)*

**Repo:** `bahmni-mart`
**Location analysed:** `/Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/bahmni-mart`
**Analysis date:** 2026-02-20
**Verdict: No changes required — zero OpenELIS dependency**

---

## What is bahmni-mart?

bahmni-mart is a standalone Spring Boot batch application (Java 8, Gradle) that performs ETL from OpenMRS MySQL into a separate analytics PostgreSQL database ("mart" database). It feeds Metabase dashboards and bahmni-reports with denormalized, analytics-optimised tables.

It is **not** an integration layer. It is a read-only analytics pipeline.

---

## Does bahmni-mart connect to OpenELIS?

**No.** Zero references to `clinlims`, `openelis`, or `elis` anywhere in the codebase — no SQL files, no Java source, no configuration files.

Data sources:
| Source | Technology | Purpose |
|---|---|---|
| OpenMRS MySQL | `spring.ds-openmrs` | All clinical source data |
| Analytics PostgreSQL | `spring.ds-mart` | Destination (mart DB) |
| Spring Cloud Dataflow PostgreSQL | optional | Job tracking only |

No connection to OpenELIS PostgreSQL (`clinlims` schema) exists or is configured.

---

## What does it ETL?

All 16 jobs source from OpenMRS MySQL only:

| Job | OpenMRS tables used |
|---|---|
| Patients | `patient`, `person_details_default`, `person_address_default`, `person_attributes` |
| Programs | `program`, `patient_program`, `patient_program_data_default` |
| Visits And Encounters | `visit`, `encounter`, `encounter_type` |
| Medication And Orders | `orders`, `drug_order`, `drug`, `order_frequency` |
| Diagnoses And Conditions | `obs` (diagnosis concept class) |
| Obs Data (Form1) | `obs`, `concept`, `concept_name`, `form` |
| Form2 Obs Data | `obs`, `form_resource`, `concept` |
| Bacteriology Data | `obs` (bacteriology concept set) |
| Bed Management | `obs`, `location` |
| Operation Theater | `obs`, `encounter_type` |
| Appointments | `obs`, `visit_type` |
| Location | `location` |
| Provider | `provider`, `person_name` |
| MetaData Dictionary | `concept`, `concept_name`, `concept_reference_term_map_view` |
| Person | `person_details_default`, `person_attributes` |
| Registration Second Page | `obs`, `encounter_type` |

The "Bacteriology Data" job reads bacteriology observations from OpenMRS `obs` — not from OpenELIS directly. Any bacteriology data in OpenMRS arrived there via the existing AtomFeed sync from the old OpenELIS (and will continue to arrive via the mediator's result write-back with OEG2).

---

## Key configuration files

```
package/docker/files/application.properties     # DB connection strings
conf/bahmni-mart.json                            # Job/view/procedure definitions
conf/liquibase.xml                               # Creates markers table (event cursor tracking)
src/main/resources/readerSql/                    # 30 SQL query files (all OpenMRS tables)
src/main/resources/viewSql/                      # 13 SQL view definitions
src/main/resources/templates/ddlForForm.ftl      # Dynamic DDL generation for form-based tables
```

---

## Impact assessment

**Zero impact.** bahmni-mart is completely isolated from OpenELIS. Replacing OpenELIS with OpenELIS-Global-2 requires no changes to bahmni-mart whatsoever.

The only indirect relationship: lab results written by the mediator into OpenMRS observations will be picked up by bahmni-mart's existing Obs Data and Bacteriology Data jobs — the same mechanism that works today. No configuration change is needed.
