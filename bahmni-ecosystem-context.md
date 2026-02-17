# Bahmni Ecosystem Context: OpenELIS (bahmni-lab)

This document explains how this OpenELIS repository fits within the broader Bahmni healthcare platform, including architecture diagrams, data flows, and integration details.

## What is Bahmni?

[Bahmni](https://www.bahmni.org/) is an open-source Hospital Information System (HIS) and Electronic Medical Record (EMR) designed for low-resource hospitals and clinics. It is deployed in **500+ hospitals across 50+ countries**. Bahmni is not a single monolithic application — it is an integration layer that ties together several best-of-breed open-source healthcare systems:

| Component | Role | Technology |
|-----------|------|------------|
| **OpenMRS** | EMR / Clinical data | Java, MySQL |
| **OpenELIS** (this repo) | Laboratory Information System | Java, PostgreSQL |
| **Odoo** (formerly OpenERP) | Billing, Inventory, Pharmacy | Python, PostgreSQL |
| **dcm4chee** | Radiology / PACS / DICOM imaging | Java |
| **Bahmni Apps** | Clinical UI (registration, consultation, lab) | AngularJS |
| **Bahmni Reports** | Reporting engine | Java, MySQL/PostgreSQL |

## High-Level Architecture

```mermaid
graph TB
    subgraph "User-Facing Layer"
        BA["Bahmni Apps<br/>(AngularJS SPA)"]
        BR["Bahmni Reports"]
    end

    subgraph "Core Services"
        OMRS["OpenMRS<br/>(EMR)<br/>Port 8050"]
        ELIS["OpenELIS<br/>(Lab - this repo)<br/>Port 8052"]
        ODOO["Odoo / OpenERP<br/>(Billing & Inventory)<br/>Port 8069"]
        DCM["dcm4chee<br/>(Radiology/PACS)<br/>Port 8055"]
    end

    subgraph "Databases"
        MYSQL["MySQL<br/>(OpenMRS: openmrs DB)"]
        PG_ELIS["PostgreSQL<br/>(OpenELIS: clinlims DB)"]
        PG_ODOO["PostgreSQL<br/>(Odoo DB)"]
    end

    subgraph "Integration Layer"
        AF["AtomFeed<br/>(Event Bus)"]
    end

    BA -->|"REST API"| OMRS
    BA -->|"REST API"| ELIS
    BA -->|"REST API"| ODOO
    BR -->|"SQL queries"| MYSQL
    BR -->|"SQL queries"| PG_ELIS

    OMRS --> MYSQL
    ELIS --> PG_ELIS
    ODOO --> PG_ODOO

    OMRS <-->|"AtomFeed"| AF
    ELIS <-->|"AtomFeed"| AF
    ODOO <-->|"AtomFeed"| AF

    style ELIS fill:#f9d71c,stroke:#333,stroke-width:3px
    style AF fill:#e6f3ff,stroke:#333
```

## AtomFeed Integration: The Backbone

All Bahmni services communicate via **AtomFeed** — a publish/subscribe mechanism based on the [Atom Syndication Format (RFC 4287)](https://tools.ietf.org/html/rfc4287). Each service publishes state changes as feed entries. Background worker threads in other services poll these feeds and process new events.

```mermaid
sequenceDiagram
    participant Doc as Doctor<br/>(Bahmni Apps)
    participant OMRS as OpenMRS<br/>(EMR)
    participant AF as AtomFeed<br/>(Event Bus)
    participant ELIS as OpenELIS<br/>(Lab)
    participant ODOO as Odoo<br/>(Billing)

    Doc->>OMRS: 1. Register patient
    OMRS->>AF: Publish patient event
    AF-->>ELIS: Patient feed consumed
    ELIS->>ELIS: Create/update patient in clinlims
    AF-->>ODOO: Patient feed consumed
    ODOO->>ODOO: Create customer record

    Doc->>OMRS: 2. Order lab tests
    OMRS->>AF: Publish lab order event
    AF-->>ELIS: Lab order feed consumed
    ELIS->>ELIS: Create accession + samples
    AF-->>ODOO: Order feed consumed
    ODOO->>ODOO: Create quotation/invoice

    Note over ELIS: Lab technician processes samples,<br/>enters results, validates

    ELIS->>AF: 3. Publish results event
    AF-->>OMRS: Results feed consumed
    OMRS->>OMRS: Store as lab encounter/obs
    Doc->>OMRS: 4. View results in EMR
```

## Data Flow Table (AtomFeed)

This table shows exactly what data flows between services via AtomFeed:

| Source | Destination | Source Resource | Destination Resource | Direction |
|--------|-------------|-----------------|---------------------|-----------|
| OpenMRS | **OpenELIS** | Patient | Patient (in clinlims) | One-way |
| OpenMRS | **OpenELIS** | Lab Order (Encounter) | Draft Accession + Samples | One-way |
| OpenMRS | **OpenELIS** | Concept (test definitions) | Sample Types, Lab Tests, Panels | One-way |
| **OpenELIS** | OpenMRS | Accession, Samples & Results | Encounter (lab results as Obs) | One-way |
| OpenMRS | Odoo | Patient | Customer | One-way |
| OpenMRS | Odoo | Orders (drug/lab) | Quotation / Sale Order | One-way |
| OpenMRS | Odoo | Concept (drug definitions) | Drug / Product | One-way |

## Lab Order Lifecycle (Detailed)

This diagram shows the complete lifecycle of a lab order from the doctor's desk through the lab and back:

```mermaid
flowchart TD
    A["Doctor orders lab test<br/>(Bahmni clinical UI)"] --> B["OpenMRS saves order<br/>as Encounter + Obs"]
    B --> C["OpenMRS publishes<br/>encounter event to AtomFeed"]
    C --> D["OpenELIS EncounterFeedWorker<br/>polls & consumes event"]
    D --> E["OpenELIS fetches full<br/>encounter from OpenMRS REST API"]
    E --> F{"Patient exists<br/>in OpenELIS?"}
    F -->|No| G["Create patient in clinlims<br/>(PatientFeedEventWorker)"]
    F -->|Yes| H["Update patient if needed"]
    G --> I["Create Accession<br/>(lab order in OpenELIS)"]
    H --> I
    I --> J["Create Sample(s)<br/>linked to accession"]
    J --> K["Create Analysis records<br/>(one per test ordered)"]

    K --> L["Lab tech sees pending<br/>tests on worklist"]
    L --> M["Collect & process samples"]
    M --> N["Enter test results"]
    N --> O["Validate results<br/>(supervisor review)"]
    O --> P["Results marked as Final"]

    P --> Q["OpenELIS publishes<br/>results event to AtomFeed"]
    Q --> R["OpenMRS ResultFeedWorker<br/>consumes event"]
    R --> S["OpenMRS creates<br/>lab result Encounter + Obs"]
    S --> T["Doctor views results<br/>in Bahmni clinical UI"]

    style A fill:#e8f5e9
    style T fill:#e8f5e9
    style I fill:#fff9c4
    style P fill:#fff9c4
```

## OpenELIS Codebase Structure in Bahmni Context

This repo is a fork of **OpenELIS v3.1** (circa 2013, commit `8f8ab686`). The Bahmni team's changes span two areas:

```mermaid
graph TB
    subgraph "Original OpenELIS v3.1 (1,447 Java files)"
        CORE["us.mn.state.health.lims.*<br/>Core LIS functionality"]
    end

    subgraph "org.bahmni.* — New Bahmni Package (77 files, ~6,400 LOC)"
        AF_PKG["AtomFeed Integration<br/>org.bahmni.feed.openelis<br/>~5,925 LOC"]
        REST["REST/JSON APIs<br/>org.bahmni.openelis.domain<br/>~503 LOC"]
    end

    subgraph "us.mn.state.health.lims.* — Bahmni Changes in Original Package"
        NEW["132 new files added<br/>(dashboard, upload, health center,<br/>REST handlers, provider query)"]
        MOD["1,522 files modified<br/>(nearly every original file)"]
    end

    DB["Database Additions<br/>7 new tables<br/>(markers, failed_events, etc.)"]

    CORE --- NEW
    CORE --- MOD
    CORE --- AF_PKG
    CORE --- REST
    CORE --- DB

    style AF_PKG fill:#ffccbc,stroke:#333
    style REST fill:#c8e6c9,stroke:#333
    style NEW fill:#fff3e0,stroke:#333
    style MOD fill:#fce4ec,stroke:#333
    style DB fill:#bbdefb,stroke:#333
```

> **Important:** The `org.bahmni` package is NOT the full picture of Bahmni's changes. The team also added 132 new files and modified 1,522 of the original 1,447 files directly in `us.mn.state.health.lims`. Key feature additions inside the original package include: dashboard views, CSV bulk upload, health center management, REST/JSON handlers, and provider autocomplete.

### AtomFeed Integration Classes

The largest Bahmni customization (~5,925 LOC) handles bidirectional sync with OpenMRS:

| Class / Package | Purpose |
|----------------|---------|
| `PatientFeedEventWorker` | Consumes patient events from OpenMRS, creates/updates patients in clinlims |
| `EncounterFeedWorker` | Consumes lab order events from OpenMRS, creates accessions/samples |
| `OpenElisPatientEventPublisher` | Publishes patient updates back to OpenMRS (if local edits) |
| `OpenElisAccessionEventPublisher` | Publishes accession results back to OpenMRS |
| `OpenMRSPatientMapper` | Maps OpenMRS patient JSON to OpenELIS patient model |
| `OpenMRSEncounterMapper` | Maps OpenMRS encounter (lab order) to OpenELIS accession |
| `ResultEventPublisher` | Publishes validated lab results to OpenMRS |
| `FeedReaderScheduler` (Quartz) | Background job polling AtomFeed every few seconds |
| `atomfeed.properties` | Configuration: OpenMRS feed URLs, polling intervals |

### REST/JSON API Classes

| Class | Purpose |
|-------|---------|
| `AccessionDetail` / `AccessionNote` | Value objects for lab accession API responses |
| `CompletePatientDetails` | Full patient data for API responses |
| `TestDetail` | Lab test data for API responses |
| `PatientHandler` | REST handler for patient queries |
| `AccessionHandler` | REST handler for accession/results queries |
| `WebServiceAction` | Base Struts action providing authentication for REST calls |

### Database Additions (13 tables in `BahmniConfig.xml`)

**AtomFeed infrastructure (7 tables):**

| Table | Purpose |
|-------|---------|
| `markers` | Tracks AtomFeed consumption position (last processed entry) |
| `failed_events` | Stores events that failed processing for retry |
| `failed_event_retry_log` | Retry attempt history for failed events |
| `chunking_history` | Tracks large feed chunking state |
| `event_records` | Locally published events (OpenELIS → OpenMRS) |
| `event_records_offset_marker` | Offset tracking for published events |
| `event_records_queue` | Event queue for async processing |

**Feature tables (6 tables):**

| Table | Purpose |
|-------|---------|
| `external_reference` | Maps OpenMRS UUIDs to internal OpenELIS IDs |
| `HEALTH_CENTER` | Health center / facility management |
| `sample_source` | Sample source dropdown values (e.g., ward, OPD) |
| `import_status` | CSV/bulk upload status tracking |
| `type_of_test_status` | Custom test status type definitions |
| `test_status` | Test-level status tracking |

## Deployment Architecture

```mermaid
graph TB
    subgraph "Docker Containers"
        PROXY["Reverse Proxy<br/>(Apache HTTPD / nginx)<br/>Port 80/443"]
        OMRS_C["OpenMRS<br/>Port 8050"]
        ELIS_C["OpenELIS (this repo)<br/>Tomcat 8.0.42<br/>Port 8052"]
        ODOO_C["Odoo<br/>Port 8069"]
        ERP_CON["ERP Connect<br/>Port 8053"]
        REPORTS["Bahmni Reports<br/>Port 8051"]
        BA_C["Bahmni Apps<br/>(static files served by proxy)"]
    end

    subgraph "Database Containers"
        MYSQL_C["MySQL<br/>(openmrs DB)<br/>Port 3306"]
        PG_C["PostgreSQL<br/>(clinlims DB + Odoo DB)<br/>Port 5432"]
    end

    PROXY --> OMRS_C
    PROXY --> ELIS_C
    PROXY --> ODOO_C
    PROXY --> REPORTS
    PROXY --> BA_C

    OMRS_C --> MYSQL_C
    ELIS_C --> PG_C
    ODOO_C --> PG_C
    ERP_CON --> ODOO_C
    REPORTS --> MYSQL_C
    REPORTS --> PG_C

    OMRS_C <-->|"AtomFeed over HTTP"| ELIS_C
    OMRS_C <-->|"AtomFeed over HTTP"| ERP_CON

    style ELIS_C fill:#f9d71c,stroke:#333,stroke-width:3px
```

### Docker Images for OpenELIS

| Image | Purpose |
|-------|---------|
| `bahmni/openelis` | Application image (Amazon Corretto 8 + embedded Tomcat 8.0.42) |
| `bahmni/openelis-db:fresh` | PostgreSQL with empty clinlims schema (for new installations) |
| `bahmni/openelis-db:demo` | PostgreSQL with demo data pre-loaded |

### Key Environment Variables

| Variable | Purpose | Default |
|----------|---------|---------|
| `OPENELIS_DB_SERVER` | PostgreSQL host | localhost |
| `OPENELIS_DB_PORT` | PostgreSQL port | 5432 |
| `OPENELIS_DB_NAME` | Database name | clinlims |
| `OPENELIS_DB_USERNAME` | Database user | clinlims |
| `OPENELIS_DB_PASSWORD` | Database password | clinlims |
| `OPENMRS_HOST` | OpenMRS server host | openmrs |
| `OPENMRS_PORT` | OpenMRS server port | 8080 |

## Technology Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Language | Java | 7 source / 8 runtime (Docker uses Amazon Corretto 8) |
| Build | Apache Ant | >= 1.9.1 |
| Web Framework | Struts 1.x + Tiles | 1.x |
| ORM | Hibernate | 3.x |
| Database | PostgreSQL | 9.x+ |
| Connection Pool | C3P0 | - |
| View | JSP | 2.x |
| CSS Preprocessor | SASS (via Ruby Compass) | Ruby 3.1 |
| DB Migrations | Liquibase | 1.9.5 |
| Scheduling | Quartz | 2.x |
| Server | Tomcat (embedded in Docker) | 8.0.42 |
| Source Encoding | ISO-8859-1 | - |

## Concept Sync: How Test Definitions Flow

Lab test definitions originate in OpenMRS and are synced to OpenELIS via AtomFeed:

```mermaid
flowchart LR
    subgraph "OpenMRS"
        C["Concept Dictionary<br/>(test definitions, panels,<br/>sample types)"]
    end

    subgraph "AtomFeed"
        CF["Concept Feed<br/>/openmrs/ws/atomfeed/concept/"]
    end

    subgraph "OpenELIS (clinlims DB)"
        ST["sample_type_of_sample"]
        LT["test<br/>(lab test definitions)"]
        LP["panel<br/>(test panels/profiles)"]
        PT["panel_test<br/>(panel ↔ test mapping)"]
    end

    C -->|"Publish"| CF
    CF -->|"ConceptFeedWorker<br/>consumes & maps"| ST
    CF -->|"ConceptFeedWorker<br/>consumes & maps"| LT
    CF -->|"ConceptFeedWorker<br/>consumes & maps"| LP
    CF -->|"ConceptFeedWorker<br/>consumes & maps"| PT
```

## Migration Context: Why This Matters

This repository is being evaluated for migration to [OpenELIS-Global-2](https://github.com/DIGI-UW/OpenELIS-Global-2) (v3.2.x). The key architectural difference:

```mermaid
graph LR
    subgraph "Current: Bahmni OpenELIS (this repo)"
        A1["OpenMRS"] <-->|"AtomFeed<br/>(Atom RSS)"| A2["OpenELIS v3.1"]
    end

    subgraph "Target: OpenELIS-Global-2"
        B1["OpenMRS +<br/>FHIR2 Module"] <-->|"FHIR R4<br/>(HL7 Standard)"| B2["OpenELIS-Global v3.2"]
    end

    A2 -.->|"Migration"| B2

    style A2 fill:#ffccbc
    style B2 fill:#c8e6c9
```

| Aspect | Current (this repo) | Target (OE-Global-2) |
|--------|-------------------|----------------------|
| Integration Protocol | AtomFeed (custom) | FHIR R4 (HL7 standard) |
| Java Version | 8 | 21 |
| Web Framework | Struts 1.x / JSP | Spring 6.2 / React |
| Build System | Ant | Maven |
| ORM | Hibernate 3.x | Hibernate 5.6 |
| Frontend | JSP + jQuery | React + Carbon Design |
| FHIR Support | None | HAPI FHIR 6.6.2 |

For the detailed migration analysis, see [migration-analysis-openelis-global-v3.md](./migration-analysis-openelis-global-v3.md).

## Quick Reference: Key File Locations

| What | Where |
|------|-------|
| Java source | `openelis/src/us/mn/state/health/lims/` |
| Bahmni AtomFeed code | `openelis/src/org/bahmni/feed/openelis/` |
| Bahmni REST/domain code | `openelis/src/org/bahmni/openelis/` |
| Hibernate config | `openelis/src/us/mn/state/health/lims/hibernate/hibernate.cfg.xml` |
| Struts config | `openelis/WebContent/WEB-INF/struts-config.xml` |
| Struts Bahmni extensions | `openelis/WebContent/WEB-INF/struts-globalOpenELIS.xml` |
| AtomFeed config | `openelis/WebContent/WEB-INF/classes/atomfeed.properties` |
| Liquibase migrations | `liquibase/` |
| Build file | `build.xml` |
| Docker resources | `package/docker/` |
| Helm chart | `package/helm/` |
| DB dumps | `db_backup/` |
