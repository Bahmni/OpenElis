# Architecture Detail: Containers, Config, and Deployment

*Back to [Integration Plan](../bahmni-openelis-global2-integration-plan.md)*

---

Two architecture options are proposed. See [Architecture Decision](../bahmni-openelis-global2-integration-plan.md#5-architecture-decision-full-openhie-vs-simplified) for the comparison table and recommendation.

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

### Containers (6 new)

| Container Name | Purpose | Notes |
|---|---|---|
| `openelisglobal-webapp` | OE-Global-2 Java backend (v3.2.1.1) | Replaces `bahmni/openelis` |
| `openelisglobal-database` | OE-Global-2 PostgreSQL database | Replaces `bahmni/openelis-db` |
| `external-fhir-api` | OE-Global-2's HAPI FHIR store — **also serves as the shared FHIR store** | Receives writes from Lab on FHIR |
| `openelisglobal-front-end` | React SPA frontend | New |
| `openelisglobal-proxy` | nginx reverse proxy | New |
| `oe-certs` | SSL certificate generator | Init container |

### Config

```properties
# OpenMRS Lab on FHIR — push directly to OE-Global-2's FHIR store
labonfhir.lisUrl=http://external-fhir-api:8080/fhir/
labonfhir.activateFhirPush=true
labonfhir.authType=NONE
labonfhir.labUpdateTriggerObject=Order

# OE-Global-2 — poll its own FHIR store
org.openelisglobal.remote.source.uri=http://external-fhir-api:8080/fhir/
org.openelisglobal.remote.poll.frequency=20000
org.openelisglobal.remote.source.identifier=Practitioner/*
org.openelisglobal.remote.source.updateStatus=true
org.openelisglobal.task.useBasedOn=true
org.openelisglobal.fhir.subscriber=http://external-fhir-api:8080/fhir/
org.openelisglobal.fhir.subscriber.resources=Task,Patient,ServiceRequest,DiagnosticReport,Observation,Specimen,Practitioner,Encounter
```

### Auth

Docker network isolation — services communicate on an internal Docker network not exposed externally. No auth overhead for PoC/internal deployments.

---

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

### Containers (12 new)

**OE-Global-2 (6):**

| Container Name | Purpose |
|---|---|
| `openelisglobal-webapp` | OE-Global-2 Java backend (v3.2.1.1) |
| `openelisglobal-database` | OE-Global-2 PostgreSQL database |
| `external-fhir-api` | OE-Global-2's internal HAPI FHIR store |
| `openelisglobal-front-end` | React SPA frontend |
| `openelisglobal-proxy` | nginx reverse proxy |
| `oe-certs` | SSL certificate generator (init container) |

**Health Information Exchange (6):**

| Container Name | Purpose |
|---|---|
| `openhim-core` | FHIR routing proxy + auth (API gateway) |
| `openhim-console` | OpenHIM admin UI (port 9000) |
| `openhim-config` | Auto-configures OpenHIM channels/clients (init container) |
| `openhim-mongo` | MongoDB for OpenHIM state |
| `shr-hapi-fhir` | Shared Health Record (HAPI FHIR server) |
| `hapi-fhir-db` | PostgreSQL for SHR |

### Config

From the [reference implementation](https://github.com/DIGI-UW/openelis-openmrs-hie):

```properties
# OpenMRS Lab on FHIR — push to SHR via OpenHIM
labonfhir.lisUrl=http://openhim-core:5001/fhir/
labonfhir.activateFhirPush=true
labonfhir.authType=BASIC
labonfhir.username=OpenMRS
labonfhir.password=admin
labonfhir.labUpdateTriggerObject=Order

# OE-Global-2 — poll SHR via OpenHIM
org.openelisglobal.remote.source.uri=http://openhim-core:5001/fhir/
org.openelisglobal.remote.poll.frequency=20000
org.openelisglobal.remote.source.identifier=Practitioner/*
org.openelisglobal.remote.source.updateStatus=true
org.openelisglobal.task.useBasedOn=true
org.openelisglobal.fhir.subscriber=http://openhim-core:5001/fhir/
org.openelisglobal.fhir.subscriber.resources=Task,Patient,ServiceRequest,DiagnosticReport,Observation,Specimen,Practitioner,Encounter
```

### Auth

OpenHIM provides basic auth + audit trail. Pre-configured clients:
- **`OpenMRS`** — OpenMRS authenticates to OpenHIM
- **`OpenELIS`** — OE-Global-2 authenticates to OpenHIM
- OpenHIM routes all `/fhir/*` requests to the SHR

```properties
# OpenMRS credentials
labonfhir.authType=BASIC
labonfhir.username=OpenMRS
labonfhir.password=admin

# OE-Global-2 credentials
org.openelisglobal.fhirstore.username=OpenELIS
org.openelisglobal.fhirstore.password=admin
```

---

## Current Containers (being replaced)

| Container | Image | Purpose |
|---|---|---|
| `bahmni/openelis` | WAR on Tomcat, port 8052 | OpenELIS Bahmni fork |
| `bahmni/openelis-db` | PostgreSQL | OpenELIS database |

**Net change:** 2 containers removed, **6 added (Option B)** or **12 added (Option A)**.
