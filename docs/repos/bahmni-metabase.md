# Repository Analysis: bahmni-metabase

*Back to [Integration Plan](../../bahmni-openelis-global2-integration-plan.md)*

**Analysed:** 2026-02-19 | **Repo:** bahmni-metabase

---

## Role in the Integration

`bahmni-metabase` is a Docker/Helm-packaged Metabase deployment that provides analytics dashboards for Bahmni installations.

**Impact on OEG2 migration: None.** bahmni-metabase has **no connection to OpenELIS**. No lab dashboards exist. All existing dashboards query OpenMRS or Bahmni Mart. No changes are required.

---

## Database Connections

Metabase is configured to connect to **two databases only**:

| Name | Type | Target |
|---|---|---|
| `openmrs` | MySQL | OpenMRS database (`OPENMRS_DB_HOST`, `OPENMRS_DB_NAME`) |
| `mart` | PostgreSQL | Bahmni Mart database (`MART_DB_HOST`, `MART_DB_NAME`) |

OpenELIS PostgreSQL is **not configured** and has never been connected.

**Configuration file:** `package/docker/scripts/database/add_databases.sh`

---

## Dashboards / Reports

Four predefined reports, all clinical/patient-focused:

| Report | Database | Visualization | Data |
|---|---|---|---|
| Registered Patients | OpenMRS | Table (pivot on Gender) | Patient demographics, last 30 days |
| Clinic Visits | OpenMRS | Table (pivot on Visit type) | Visit counts, last 15 days |
| Number of Diagnosis | Mart | Table | Coded diagnosis counts, last 7 days |
| Old vs New Patients | OpenMRS | Pie chart | New vs returning patient count |

**SQL files:** `package/docker/scripts/reports/sql/`

**OpenMRS tables queried:** `patient`, `person`, `person_name`, `patient_identifier`, `patient_identifier_type`, `visit`, `visit_type`, `person_address`, `person_attribute`, `global_property`

**Mart tables queried:** `visit_diagnoses`

**Lab-related tables queried: none.**

---

## Architecture

Metabase version: **v0.50.25**. Deployment model: Docker image + Helm chart with programmatic setup.

Startup sequence:
1. `metabase_startup.sh` — starts Metabase server
2. `metabase_init.sh` — waits for port 3000, then runs setup scripts
3. `create_admin.sh` — creates admin user via REST API
4. `add_databases.sh` — registers OpenMRS + Mart via REST API
5. `create_metabase_collection.sh` — creates "Bahmni Analytics" collection
6. `add_reports.sh` + `old_vs_new_patient_report.sh` — creates 4 reports via REST API

Reports are defined in `report_inputs.json` (name, SQL filename, db_ref_name), injected via `jq` into REST API calls to `http://localhost:3000/api/card`.

---

## Changes Required

**None.** No OpenELIS connection, no lab dashboards, zero impact from OEG2 migration.

If lab analytics dashboards are wanted post-migration, new SQL reports can be added by:
1. Adding OEG2 (or OpenMRS) as a third database in `add_databases.sh`
2. Adding new SQL files and entries in `report_inputs.json`

---

## Key Files

| File | Purpose |
|---|---|
| `package/docker/scripts/database/add_databases.sh` | Registers OpenMRS + Mart with Metabase |
| `package/docker/scripts/reports/request/report_inputs.json` | Report definitions (name, SQL, DB) |
| `package/docker/scripts/reports/sql/` | 4 SQL files — none reference clinlims |
| `package/docker/Dockerfile` | `FROM metabase/metabase:v0.50.25` |
| `package/helm/values.yaml` | Environment variable defaults |
