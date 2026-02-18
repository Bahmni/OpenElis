# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

OpenElis is a Laboratory Information System (LIS) forked from OpenELIS v3.1, customized for the Bahmni healthcare platform. It integrates with OpenMRS (EMR) and OpenERP via AtomFeed-based event synchronization.

## Build System

This project uses **Apache Ant** (not Maven/Gradle). The non-standard project layout prevents Maven usage.

### Prerequisites
- Java 7 source compatibility (build.xml: `source="1.7" target="1.7"`); Docker runtime uses Amazon Corretto 8
- Ant >= 1.9.1
- Ruby 3.1+ with `gem install compass` (for SASS compilation)
- PostgreSQL (for database targets)

### Common Build Commands

```bash
ant dist                        # Build openelis.war (clean + compile + package)
ant compile                     # Compile Java, JasperReports, and SASS
ant java.compile                # Compile Java only (fastest iteration)
ant test-only                   # Run unit/integration tests (assumes already compiled)
ant test                        # Clean + compile + updateDB (prepares for test-only)
ant setupDB test test-only dist # Full build: init DB + migrate + test + WAR
ant report                      # Generate JUnit HTML report at junit/html/index.html
ant updateDB                    # Run Liquibase migrations (bahmni context)
ant resetDB                     # Drop + init + migrate database
```

### Running a Single Test
There is no built-in single-test target. Tests run via JUnit batch in `ant test-only`. To run a single test, modify the `<batchtest>` fileset in `build.xml` temporarily, or use your IDE's JUnit runner with classpath including `openelis/target`, `openelis/WebContent/WEB-INF/lib/*.jar`, and `openelis/test/lib/*.jar`.

## Project Structure

```
openelis/src/          # Java source (package: us.mn.state.health.lims.*, org.bahmni.*)
openelis/test/         # Test source (*Test.java, *IT.java)
openelis/WebContent/   # Web root: JSP pages, WEB-INF config, JS, CSS, JasperReports
openelis/target/       # Compiled classes (generated)
openelis/dist/         # WAR output (generated)
liquibase/             # Database migrations (Liquibase 1.9.5, changeLogs.xml)
scripts/               # Shell scripts for DB init, deployment
package/               # Docker, Helm chart, deployment resources
db_backup/             # PostgreSQL dump files (bahmni-base.dump, haiti, demo)
build.xml              # Ant build file
build.properties       # Build paths configuration
```

## Architecture

### Web Framework: Struts 1.x + Tiles
- **Actions** extend `BaseAction` (abstract) — entry point for all requests
- **Forms** are Struts ActionForms configured in `struts-config.xml`
- **Views** are JSP pages composed via Tiles (`tiles-defs.xml`)
- Bahmni extensions in `struts-globalOpenELIS.xml`
- Validation rules in `validation.xml`

### ORM: Hibernate 3.x
- Config: `openelis/src/us/mn/state/health/lims/hibernate/hibernate.cfg.xml`
- PostgreSQL with C3P0 connection pool
- `.hbm.xml` mapping files alongside entity classes
- Java source encoding: ISO-8859-1

### Package Organization
Source is organized by domain module under `us.mn.state.health.lims`:
- Each module has `action/`, `dao/`, `daoimpl/`, `valueholder/` sub-packages
- Key modules: `patient`, `sample`, `analysis`, `result`, `test`, `analyzer`, `login`
- Bahmni integrations: `org.bahmni.feed.openelis`, `org.bahmni.openelis`
- REST/web services: `us.mn.state.health.lims.ws`

**Note on code provenance:** The original fork (commit `8f8ab686`, June 2013) contained 1,447 Java files entirely in `us.mn.state.health.lims`. The Bahmni team added 77 files in `org.bahmni.*` (AtomFeed + REST), 132 new files in `us.mn.state.health.lims.*` (dashboard, CSV upload, health center, REST handlers), and modified 1,522 of the original 1,447 files.

### Integration Points
- **AtomFeed**: Bi-directional sync with OpenMRS for patient/encounter/lab-order events
- **REST endpoints**: Patient and LabResults APIs under `ws/` package
- **Analyzers**: Equipment integration for automated result import

## Database

- **PostgreSQL**, schema: `clinlims`, database: `clinlims`
- Init from dump: `scripts/initDB.sh` (restores `db_backup/bahmni-base.dump`)
- Migrations: Liquibase 1.9.5 with context-based changelogs (`bahmni`, `haiti`, etc.)
- Migration command: `ant updateDB` runs Liquibase against `localhost:5432/clinlims`

## Docker Deployment

- App image: `bahmni/openelis` (Amazon Corretto 8, embedded Tomcat 8.0.42, port 8052)
- DB images: `bahmni/openelis-db:fresh` and `bahmni/openelis-db:demo`
- Runtime config via environment variables: `OPENELIS_DB_SERVER`, `OPENELIS_DB_PORT`, `OPENELIS_DB_NAME`, `OPENELIS_DB_USERNAME`, `OPENELIS_DB_PASSWORD`, `OPENMRS_HOST`, `OPENMRS_PORT`
- Templates in `package/docker/openelis/templates/` are substituted at startup via `envsubst`
- Helm chart in `package/helm/`

## Documentation Preferences
- All sequence diagrams and flowcharts must use **Mermaid.js** syntax (```mermaid blocks). No ASCII art diagrams.

## Known Technical Issues
- Transaction and Hibernate session management is fragile
- Pagination state stored in HttpSession (not URL-based)
- Duplicated pagination code across ResultValidationPaging, ResultsPaging, AnalyzerResultsPaging
