# Repository Analysis: openmrs-distro-bahmni

*Back to [Integration Plan](../../bahmni-openelis-global2-integration-plan.md)*

**Analysed:** 2026-02-19 | **Repo:** openmrs-distro-bahmni

---

## Role in the Integration

`openmrs-distro-bahmni` is a **Maven-based build repository** that assembles the Bahmni OpenMRS Docker image and Helm chart. It is **not** a Docker Compose orchestration repo — it builds a single container image (`bahmni/openmrs`) and its corresponding Helm chart. The full multi-container Bahmni stack (including OpenELIS, proxy, etc.) is orchestrated elsewhere.

**What this repo does:** Compiles OpenMRS modules into a deployable image, bundles Bahmni-specific `.omod` files, generates runtime property files from environment-variable templates, and publishes to Docker Hub + Bahmni Helm registry.

**Impact:** Changes are needed to remove the OpenELIS AtomFeed integration from the OpenMRS image. The mediator handles integration state externally; OpenMRS itself does not need to know about OEG2 directly.

---

## Important Clarification

The integration plan described this repo as "the Docker Compose packaging repo" where OEG2 containers would be added. **This is incorrect.** This repo only builds the OpenMRS image. The full container orchestration stack (all services, networking, volumes, proxy) lives in a separate repository (likely `bahmni-docker` or `bahmni-devops` — not yet deep-dived). Adding OEG2 containers is a task for that orchestration repo, not this one.

---

## OpenELIS References in This Repo

### 1. OpenMRS modules bundled in the image

**File:** `distro/pom.xml`

```xml
<dependency>
    <groupId>org.bahmni.module</groupId>
    <artifactId>openelis-atomfeed-client-omod</artifactId>
    <version>${bahmniCoreVersion}</version>
</dependency>

<dependency>
    <groupId>org.bahmni.module</groupId>
    <artifactId>elis-fhir-result-support-omod</artifactId>
    <version>${elisFhirSupportVersion}</version>
</dependency>
```

**File:** `package/docker/openmrs/Dockerfile` (lines 40–41)

```dockerfile
COPY distro/target/distro/openelis-atomfeed-client*.omod  ${OPENMRS_APPLICATION_DATA_DIRECTORY}/modules
COPY distro/target/distro/elis-fhir-result-support-*.omod ${OPENMRS_APPLICATION_DATA_DIRECTORY}/modules
```

- `openelis-atomfeed-client-omod` — the OpenMRS module that consumes the OpenELIS AtomFeed (result events from OpenELIS into OpenMRS). **Must be removed** — the mediator replaces this.
- `elis-fhir-result-support-omod` — FHIR result support module. **Needs investigation:** may be useful for OEG2 if it handles reading FHIR results back into OpenMRS, or may conflict. Evaluate in Phase 1 PoC.

### 2. bahmnicore.properties template

**File:** `package/docker/openmrs/templates/bahmnicore.properties.template`

```properties
openelis.uri=http://${OPENELIS_HOST}:${OPENELIS_PORT}/
openelis.user=${OPENELIS_ATOMFEED_USER}
openelis.password=${OPENELIS_ATOMFEED_PASSWORD}
patient.feed.uri=http://${OPENELIS_HOST}:${OPENELIS_PORT}/openelis/ws/feed/patient/recent
```

These configure bahmni-core's AtomFeed client to consume OpenELIS feeds. **Must be removed or replaced** — the mediator takes over this role.

### 3. Startup script and marker update

**File:** `package/docker/openmrs/bahmni_startup.sh` (line 22)

```bash
./update_elis_host_port.sh
```

**File:** `package/docker/openmrs/update_elis_host_port.sh`

```bash
run_sql "update markers set feed_uri = 'http://${OPENELIS_HOST}:${OPENELIS_PORT}/openelis/ws/feed/patient/recent' where feed_uri like '%openelis/ws/feed/patient/recent';"
run_sql "update failed_events set feed_uri = 'http://${OPENELIS_HOST}:${OPENELIS_PORT}/openelis/ws/feed/patient/recent' where feed_uri like '%openelis/ws/feed/patient/recent';"
```

This updates the OpenMRS `markers` and `failed_events` tables (AtomFeed consumer state) with the current OpenELIS host/port at startup. **Must be removed** — no longer relevant when the mediator manages integration state externally.

### 4. Helm chart configuration

**File:** `package/helm/openmrs/values.yaml`

```yaml
config:
  OPENELIS_HOST: openelis
  OPENELIS_PORT: '8052'
```

**File:** `package/helm/openmrs/templates/secrets.yaml`

```yaml
OPENELIS_ATOMFEED_USER: {{ .Values.secrets.OPENELIS_ATOMFEED_USER | b64enc | quote }}
OPENELIS_ATOMFEED_PASSWORD: {{ .Values.secrets.OPENELIS_ATOMFEED_PASSWORD | b64enc | quote }}
```

**Must be removed** — replaced with mediator configuration if the mediator needs any OpenMRS-side configuration.

---

## Changes Required

| Item | Action | Phase |
|---|---|---|
| Remove `openelis-atomfeed-client-omod` from `distro/pom.xml` and Dockerfile | Delete | Phase 4 |
| Evaluate and decide on `elis-fhir-result-support-omod` | Investigate in Phase 1 PoC — keep if it supports FHIR result ingestion from mediator, remove otherwise | Phase 1 |
| Remove OpenELIS properties from `bahmnicore.properties.template` | Delete 4 lines | Phase 4 |
| Remove `update_elis_host_port.sh` call from `bahmni_startup.sh` | Delete line 22 | Phase 4 |
| Delete `update_elis_host_port.sh` | Delete file | Phase 4 |
| Remove `OPENELIS_HOST`, `OPENELIS_PORT` from `values.yaml` and `configMap.yaml` | Delete | Phase 4 |
| Remove `OPENELIS_ATOMFEED_USER`, `OPENELIS_ATOMFEED_PASSWORD` from `values.yaml` and `secrets.yaml` | Delete | Phase 4 |

**Note:** Adding OEG2 containers to the deployment stack is a task for the Docker Compose/orchestration repo (not this repo). This repo only needs its OpenELIS-specific code removed.

---

## Architecture

**Build system:** Maven (mvnw). `distro/pom.xml` declares all OpenMRS module dependencies. `mvn clean install` assembles them into `distro/target/distro/`.

**Container:** `FROM openmrs/openmrs-core:2.6.x-nightly`. Port 8080 (app), 8280 (JMX). Modules copied into `${OPENMRS_APPLICATION_DATA_DIRECTORY}/modules`.

**Runtime config:** `bahmni_startup.sh` calls `envsubst` to substitute environment variables into property templates, then calls `update_elis_host_port.sh`, then starts Tomcat.

**Persistent volumes (Helm):**

| Volume | Mount | Size |
|---|---|---|
| `openmrs-patient-images-pvc` | `/home/bahmni/patient_images` | 2Gi |
| `openmrs-document-images-pvc` | `/home/bahmni/document_images` | 2Gi |
| `openmrs-clinical-forms-pvc` | `/home/bahmni/clinical_forms` | 1Gi |
| `bahmni-uploaded-results-pvc` | `/home/bahmni/uploaded_results` | 2Gi |
| `bahmni-config-pvc` | `/etc/bahmni_config` | — |

**CI/CD:** GitHub Actions → `./mvnw clean install` → Docker build/push to `bahmni/openmrs` → Helm package/publish to `bahmni/helm-charts`.

---

## Key Files

| File | Purpose |
|---|---|
| `distro/pom.xml` | Module dependencies (include openelis-atomfeed-client, elis-fhir-result-support) |
| `package/docker/openmrs/Dockerfile` | Image build; copies .omod files |
| `package/docker/openmrs/bahmni_startup.sh` | Container entrypoint; calls update_elis_host_port.sh |
| `package/docker/openmrs/update_elis_host_port.sh` | Updates AtomFeed markers in OpenMRS DB |
| `package/docker/openmrs/templates/bahmnicore.properties.template` | bahmni-core config: OpenELIS URI + credentials |
| `package/helm/openmrs/values.yaml` | Helm defaults: OPENELIS_HOST, OPENELIS_PORT |
| `package/helm/openmrs/templates/configMap.yaml` | Helm ConfigMap: OPENELIS_HOST, OPENELIS_PORT |
| `package/helm/openmrs/templates/secrets.yaml` | Helm Secrets: OPENELIS_ATOMFEED_USER/PASSWORD |
