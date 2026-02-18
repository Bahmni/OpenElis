# Architecture Detail: Container Inventory and Deployment

*Back to [Integration Plan](../bahmni-openelis-global2-integration-plan.md)*

---

Two architecture options are under consideration. See [Architecture Decision](../bahmni-openelis-global2-integration-plan.md#5-architecture-decision-full-openhie-vs-simplified) in the main plan for the comparison table and recommendation.

## Option B: Simplified (recommended)

```mermaid
flowchart TB
    subgraph bahmni["Bahmni"]
        UI[Bahmni UI<br/>bahmniapps]
        MRS[OpenMRS<br/>FHIR2 + Lab on FHIR]
        MRS_GW[OpenMRS Gateway]
        MRS_FE[OpenMRS Frontend]
        MRS_DB[(MariaDB)]
        ODOO[Odoo<br/>Billing]
    end

    subgraph oeg["OpenELIS-Global-2"]
        OEG_APP[OE-Global-2<br/>Webapp v3.2.1.1]
        OEG_FE[React Frontend]
        OEG_FHIR[HAPI FHIR<br/>external-fhir-api<br/>shared FHIR store]
        OEG_DB[(PostgreSQL)]
        OEG_PROXY[nginx Proxy]
        OEG_CERTS[Cert Generator]
    end

    UI <--> MRS_GW
    MRS_GW <--> MRS_FE
    MRS_GW <--> MRS
    MRS <--> MRS_DB
    MRS <-->|"Billing (unchanged)"| ODOO

    MRS -->|"pushes Task+ServiceRequest<br/>+Patient (FHIR bundle)"| OEG_FHIR
    MRS -.->|"polls for completed Tasks"| OEG_FHIR

    OEG_APP -->|"polls Tasks<br/>(REQUESTED)"| OEG_FHIR
    OEG_APP -->|"pushes DiagnosticReport<br/>+Observations+Task (COMPLETED)"| OEG_FHIR
    OEG_APP <--> OEG_DB
    OEG_PROXY --> OEG_FE
    OEG_PROXY --> OEG_APP

    style bahmni fill:#e8f4fd,stroke:#0d6efd
    style oeg fill:#f0f7e8,stroke:#28a745
```

**6 new containers.** OE-Global-2's `external-fhir-api` doubles as the shared FHIR store. No HIE layer.

## Option A: Full OpenHIE Stack (reference implementation)

```mermaid
flowchart TB
    subgraph bahmni["Bahmni"]
        UI[Bahmni UI<br/>bahmniapps]
        MRS[OpenMRS<br/>FHIR2 + Lab on FHIR]
        MRS_GW[OpenMRS Gateway]
        MRS_FE[OpenMRS Frontend]
        MRS_DB[(MariaDB)]
        ODOO[Odoo<br/>Billing]
    end

    subgraph hie["Health Information Exchange"]
        HIM_CORE[OpenHIM Core<br/>FHIR routing + auth]
        HIM_CONSOLE[OpenHIM Console<br/>Admin UI]
        HIM_MONGO[(MongoDB)]
        SHR[SHR - HAPI FHIR<br/>Shared Health Record]
        SHR_DB[(PostgreSQL)]
    end

    subgraph oeg["OpenELIS-Global-2"]
        OEG_APP[OE-Global-2<br/>Webapp v3.2.1.1]
        OEG_FE[React Frontend]
        OEG_FHIR[HAPI FHIR<br/>OE Local Store]
        OEG_DB[(PostgreSQL)]
        OEG_PROXY[nginx Proxy]
        OEG_CERTS[Cert Generator]
    end

    UI <--> MRS_GW
    MRS_GW <--> MRS_FE
    MRS_GW <--> MRS
    MRS <--> MRS_DB
    MRS <-->|"Billing (unchanged)"| ODOO

    MRS -->|"pushes Task+ServiceRequest<br/>+Patient (FHIR bundle)"| HIM_CORE
    HIM_CORE <--> SHR
    SHR <--> SHR_DB
    HIM_CORE <--> HIM_MONGO
    HIM_CONSOLE --> HIM_CORE

    OEG_APP -->|"polls Tasks<br/>(REQUESTED)"| HIM_CORE
    OEG_APP -->|"pushes DiagnosticReport<br/>+Observations+Task (COMPLETED)"| HIM_CORE
    OEG_APP <--> OEG_FHIR
    OEG_APP <--> OEG_DB
    OEG_PROXY --> OEG_FE
    OEG_PROXY --> OEG_APP

    style bahmni fill:#e8f4fd,stroke:#0d6efd
    style hie fill:#fff3cd,stroke:#ffc107
    style oeg fill:#f0f7e8,stroke:#28a745
```

**12 new containers.** Adds a separate SHR and OpenHIM proxy layer between OpenMRS and OE-Global-2.

## Container Inventory

### Current (Bahmni OpenELIS — being replaced)

| Container | Image | Purpose |
|---|---|---|
| `bahmni/openelis` | WAR on Tomcat, port 8052 | OpenELIS Bahmni fork |
| `bahmni/openelis-db` | PostgreSQL | OpenELIS database |

### OE-Global-2 Containers (both options)

| Container Name | Purpose | Notes |
|---|---|---|
| `openelisglobal-webapp` | OE-Global-2 Java backend (v3.2.1.1) | Replaces `bahmni/openelis` |
| `openelisglobal-database` | OE-Global-2 PostgreSQL database | Replaces `bahmni/openelis-db` |
| `external-fhir-api` | OE-Global-2's HAPI FHIR store | In Option B, also serves as the shared FHIR store |
| `openelisglobal-front-end` | React SPA frontend | New |
| `openelisglobal-proxy` | nginx reverse proxy | New |
| `oe-certs` | SSL certificate generator | New — init container |

### HIE Containers (Option A only)

| Container Name | Purpose | Notes |
|---|---|---|
| `openhim-core` | FHIR routing proxy + auth | API gateway |
| `openhim-console` | OpenHIM admin UI | Port 9000 |
| `openhim-config` | Auto-configures OpenHIM channels/clients | Init container |
| `openhim-mongo` | MongoDB for OpenHIM state | |
| `shr-hapi-fhir` | Shared Health Record (HAPI FHIR server) | Central FHIR store |
| `hapi-fhir-db` | PostgreSQL for SHR | |

**Net change:** 2 containers removed, **6 added (Option B)** or **12 added (Option A)**.

## Authentication

| Option | Auth approach | Details |
|---|---|---|
| **Option B (simplified)** | Docker network isolation | Services communicate on an internal Docker network not exposed externally. No auth overhead for PoC. |
| **Option A (full OpenHIE)** | Basic auth via OpenHIM | OpenHIM proxies FHIR endpoints; pre-configured clients: `OpenELIS` and `OpenMRS`. See config below. |
| **Either (production)** | HTTPS certificates | Mutual TLS between services for high-security deployments. Can be added later. |

### Option A auth configuration

```properties
# OpenMRS Lab on FHIR
labonfhir.authType=BASIC
labonfhir.username=OpenMRS
labonfhir.password=admin

# OE-Global-2
org.openelisglobal.fhirstore.username=OpenELIS
org.openelisglobal.fhirstore.password=admin
```

OpenHIM routes all `/fhir/*` requests to the SHR.

## Master Data Setup

| Master Data | Recommended Setup Method | Details |
|---|---|---|
| **Tests + panels** | CSV files on startup | Drop CSV in `/var/lib/openelis-global/configuration/backend/tests/`. Format: `testName,testSection,sampleType,loinc,isActive,...` |
| **Sample types** | CSV files on startup | `configuration/sampleTypes/*.csv` |
| **Dictionaries** | CSV files on startup | `configuration/dictionaries/*.csv` |
| **Result ranges** | Admin UI or REST API | No CSV import — must be configured per test via UI |
| **Organizations/centers** | FHIR import from OpenMRS | `org.openelisglobal.facilitylist.fhirstore=http://openmrs:8080/openmrs/ws/fhir2/R4` |
| **Users/providers** | FHIR import + local creation | Practitioners auto-imported from OpenMRS FHIR |
| **Roles** | CSV files on startup | `configuration/roles/*.csv` |

**Recommended approach for Bahmni:** Create a "Bahmni default" CSV configuration set checked into version control. Mount as a Docker volume.
