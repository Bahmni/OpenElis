# Repository: pacs-integration

*Back to [Integration Plan](../../bahmni-openelis-global2-integration-plan.md)*

**Repo:** `Bahmni/pacs-integration`
**Local path:** `/Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/pacs-integration`
**Deep dive date:** 2026-02-19
**Changes required:** None — used as blueprint only

---

## Role in Integration

`pacs-integration` is the **structural blueprint for the new lab order mediator service**. It is a production-deployed Bahmni standalone integration service that connects OpenMRS (via AtomFeed) to a PACS/DICOM imaging server. It uses the exact same integration pattern we need for the lab mediator.

The mediator service will replicate this structure, replacing HL7/DICOM output with FHIR bundle output to OEG2's `external-fhir-api`, and adding a result polling loop back from OEG2 into OpenMRS.

---

## Technology Stack

- **Java + Spring Boot 1.2.4** (WAR on embedded Tomcat 8.0.42)
- **Build:** Maven (multi-module: parent + webapp)
- **AtomFeed client:** `org.ict4h.atomfeed.client` v1.10.1
- **Scheduling:** Quartz 2.3.2
- **Persistence:** Spring Data JPA + PostgreSQL + Liquibase 3.10.3
- **Docker:** Amazon Corretto 8, `envsubst` for config templating

---

## Architecture Pattern

```
Quartz Scheduler (every 15s)
  ↓
FeedJob.process()
  ↓
AtomFeedClient.processEvents()     [ICT4H atomfeed-client library]
  ├── Reads cursor from markers table
  ├── Polls openmrs/ws/atomfeed/encounter/recent
  └── For each new event → EventWorker.process(event)
                              ↓
                        Fetch full encounter from OpenMRS REST
                              ↓
                        Filter orders by type
                              ↓
                        Transform → downstream message
                              ↓
                        Send to downstream system
                              ↓
                        Persist to local DB
                              ↓
                        Update cursor in markers table
```

Separate `FailedFeedJob` runs on same schedule, retrying events from `failed_events` table (max 10 retries).

---

## Key Implementation Details

### AtomFeed Subscription
- **Feed URL:** `http://openmrs:8080/openmrs/ws/atomfeed/encounter/recent`
- **Library:** `org.ict4h.atomfeed.client.AtomFeedClientFactory`
- **Cursor tracking:** `AllMarkersJdbcImpl` — persists `last_read_entry_id` to `markers` table
- **Failed events:** `AllFailedEventsJdbcImpl` — stores failures in `failed_events` table

### Scheduling
```java
// Jobs defined in quartz_cron_scheduler DB table:
// openMRSEncounterFeedJob     → 0/15 * * * * ? (every 15s)
// openMRSEncounterFailedFeedJob → 0/15 * * * * ? (every 15s)
// ScheduledTasks reads this table and registers jobs dynamically
```

### Configuration
Two properties files, templated at Docker startup via `envsubst`:

**`atomfeed.properties`:**
```properties
openmrs.encounter.feed.uri=http://${OPENMRS_HOST}:${OPENMRS_PORT}/openmrs/ws/atomfeed/encounter/recent
openmrs.auth.uri=http://${OPENMRS_HOST}:${OPENMRS_PORT}/openmrs/ws/rest/v1/session
openmrs.user=${OPENMRS_ATOMFEED_USER}
openmrs.password=${OPENMRS_ATOMFEED_PASSWORD}
feed.maxFailedEvents=10000
feed.failedEventMaxRetry=10
```

**`application.properties`:**
```properties
spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
enable.scheduling=true
```

### Database Tables
| Table | Owner | Purpose |
|---|---|---|
| `markers` | atomfeed-client lib | Cursor: last processed event ID per feed |
| `failed_events` | atomfeed-client lib | Events that failed; retried by failed job |
| `quartz_cron_scheduler` | App | Job names, cron expressions, enabled flag |
| `test_order` | App | Processed orders (deduplication + audit) |
| `order_details` | App | HL7 request/response per order |
| `modality` | App | PACS device endpoints |
| `order_type` | App | Maps OpenMRS order types → modalities |

---

## What the Mediator Replicates vs What It Adds

| Aspect | pacs-integration | Lab mediator |
|---|---|---|
| AtomFeed consumption | ✅ Same pattern | ✅ Same pattern, same library |
| Event worker | `EncounterFeedWorker` | `LabOrderFeedWorker` |
| Business logic | `PacsIntegrationService` — creates HL7, sends to PACS | `LabOrderService` — creates FHIR bundle, pushes to `external-fhir-api` |
| Output format | HL7 MWL message | FHIR Transaction Bundle (Task + ServiceRequest + Patient) |
| Retry queue | ✅ Same | ✅ Same |
| Cursor tracking | ✅ Same | ✅ Same |
| Scheduling | ✅ Same | ✅ Same |
| Local DB | ✅ Same pattern | ✅ Same pattern (different tables) |
| Result polling | ❌ Not needed (PACS pushes back) | **NEW** — poll `external-fhir-api` for completed Tasks |
| Result import | ❌ Not needed | **NEW** — push DiagnosticReport/Obs to OpenMRS via fhir2 API |
| Docker/config templating | ✅ Same | ✅ Same |

---

## Key Files to Adapt for Mediator

| Source file | Adapt to |
|---|---|
| `AtomFeedClientFactory.java` | Reuse as-is or with minor renames |
| `EncounterFeedJob.java` | → `LabOrderFeedJob.java` |
| `EncounterFailedFeedJob.java` | → `LabOrderFailedFeedJob.java` |
| `EncounterFeedWorker.java` | → `LabOrderFeedWorker.java` (replace HL7 logic with FHIR) |
| `PacsIntegrationService.java` | → `LabOrderService.java` |
| `ScheduledTasks.java` | Reuse as-is |
| `atomfeed.properties` + template | Adapt URLs and credential keys |
| `application.properties` + template | Adapt DB name, add OEG2 FHIR URL |
| `liquibase.xml` | Replace PACS tables with lab order tables |
| `Dockerfile` + `start.sh` | Reuse as-is, change app name |

**Absolute paths:**
- `pacs-integration-webapp/src/main/java/org/bahmni/module/pacsintegration/atomfeed/client/AtomFeedClientFactory.java`
- `pacs-integration-webapp/src/main/java/org/bahmni/module/pacsintegration/atomfeed/jobs/EncounterFeedJob.java`
- `pacs-integration-webapp/src/main/java/org/bahmni/module/pacsintegration/atomfeed/worker/EncounterFeedWorker.java`
- `pacs-integration-webapp/src/main/java/org/bahmni/module/pacsintegration/services/PacsIntegrationService.java`
- `pacs-integration-webapp/src/main/java/org/bahmni/module/pacsintegration/atomfeed/ScheduledTasks.java`
- `pacs-integration-webapp/src/main/resources/atomfeed.properties`
- `pacs-integration-webapp/src/main/resources/application.properties`
- `pacs-integration-webapp/src/main/resources/db/changelog/liquibase.xml`
- `package/docker/pacs-integration/Dockerfile`
- `package/docker/pacs-integration/scripts/start.sh`
