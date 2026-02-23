# Phase 1, Step 1a: Spin Up OE-Global-2

*Back to [Integration Plan](../../bahmni-openelis-global2-integration-plan.md)*

**Status:** Complete (2026-02-23)
**Goal:** Get all 6 OEG2 containers running, confirm `external-fhir-api` accepts external writes, and confirm OEG2 is polling it for Tasks.

---

## Prerequisites

- Docker Desktop running (or Docker Engine + Compose v2)
- OEG2 repo cloned at `/Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/OpenELIS-Global-2`
- Ports 80, 443, 8444, 15432 free on localhost
- Note: external FHIR store is accessed via the nginx proxy at `https://localhost/fhir/` (not directly on port 8081 — the FHIR store uses mTLS and requires a client certificate)

---

## Setup

### 1. Configure `common.properties` for FHIR polling

Edit `/Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/OpenELIS-Global-2/volume/properties/common.properties`.

Set the following properties (currently blank in the default):

```properties
# Enable OEG2 to poll external-fhir-api for Tasks (the remote FHIR source)
org.openelisglobal.remote.source.uri=https://fhir.openelis.org:8443/fhir/
org.openelisglobal.remote.source.updateStatus=true
org.openelisglobal.remote.source.identifier=Practitioner/*
org.openelisglobal.remote.poll.frequency=20000

# Enable OEG2 to push results back to external-fhir-api
org.openelisglobal.fhir.subscriber=https://fhir.openelis.org:8443/fhir/
org.openelisglobal.fhir.subscriber.resources=Task,Patient,ServiceRequest,DiagnosticReport,Observation,Specimen,Practitioner,Encounter

# Allow HTTP fallback (present in file, not used since URIs are HTTPS)
org.openelisglobal.fhir.subscriber.allowHTTP=true
```

**Notes:**
- `fhir.openelis.org` resolves to the `external-fhir-api` container **within** the Docker network (Docker's internal DNS). Do not change this hostname — it's set by the docker-compose service name.
- The webapp connects to `external-fhir-api` on **port 8443 (HTTPS)** internally. The self-signed cert is shared across all containers via the `oe-certs` init container.
- `Practitioner/*` is the wildcard Task owner — accepts Tasks for any owner. We'll narrow this down in Step 1b (open question 4).
- Poll frequency is 20s for PoC (default is 120s).

### 2. Verify `hapi_application.yaml` datasource

The file at `volume/properties/hapi_application.yaml` already has:

```yaml
datasource:
  url: 'jdbc:postgresql://db.openelis.org:5432/clinlims?currentSchema=clinlims'
  username: clinlims
  password: clinlims
```

No changes needed — `db.openelis.org` is the internal Docker DNS name for the database container.

### 3. No other config changes needed for Step 1a

The `docker-compose.yml` already provides:
- Self-signed SSL certs via `oe-certs` (shared to all containers via a named volume)
- nginx proxy on ports 80/443 — routes `/` → frontend, `/api/` → webapp, `/fhir/` → FHIR store
- `nginx-prod.conf` updated to add `/fhir/` proxy location with mTLS client cert presentation
- external-fhir-api on port **8444** externally (HTTPS only, requires mTLS client cert — not used directly)
- External FHIR access: **`https://localhost/fhir/`** via nginx proxy (nginx presents the cert)

---

## Start Up

Run from the OEG2 repo root:

```bash
cd /Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/OpenELIS-Global-2

docker compose up -d
```

Wait ~60 seconds for all containers to initialize, then check container status:

```bash
docker compose ps
```

Expected: all 6 containers in `running` (or `healthy`) state:

| Container | Expected State |
|---|---|
| `oe-certs` | `exited 0` (init container — runs once and exits) |
| `openelisglobal-database` | `healthy` |
| `external-fhir-api` | `running` |
| `openelisglobal-webapp` | `running` |
| `openelisglobal-front-end` | `running` |
| `openelisglobal-proxy` | `running` |

---

## Running the Verification

### One-command demo (recommended)

The demo script handles everything: cleanup, container startup, wait, bash verification, and Playwright UI tests.

```bash
bash /Users/vishalkarmalkar/IdeaProjects/bahmni/OpenElis/docs/steps/scripts/phase1-step1a-demo.sh
```

Script: [`docs/steps/scripts/phase1-step1a-demo.sh`](scripts/phase1-step1a-demo.sh)

To tear down after the demo:

```bash
bash /Users/vishalkarmalkar/IdeaProjects/bahmni/OpenElis/docs/steps/scripts/phase1-step1a-teardown.sh
# Add --volumes for a full clean slate (destroys database)
```

### Running suites individually

| Suite | What it covers | How to run |
|---|---|---|
| **Bash script** (Checks 1–5, 7–10) | Docker container state, DB health, FHIR API reads/writes, config, internal DNS | See below |
| **Playwright spec** (Check 6) | OEG2 web UI, login, post-login navigation, FHIR store UI | See below |

**Bash verification** (requires containers already running):

```bash
cd /Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/OpenELIS-Global-2
bash /Users/vishalkarmalkar/IdeaProjects/bahmni/OpenElis/docs/steps/scripts/phase1-step1a-verify.sh
```

**Playwright UI tests** (requires containers already running):

```bash
cd /Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/OpenELIS-Global-2/frontend

# Install Playwright browsers (first time only)
npx playwright install chromium

# Run the Step 1a UI tests
TEST_USER=admin TEST_PASS="adminADMIN!" npx playwright test bahmni-poc-step1a --reporter=list
```

Spec: [`OpenELIS-Global-2/frontend/playwright/tests/bahmni-poc-step1a.spec.ts`](../../../../openelismigration/OpenELIS-Global-2/frontend/playwright/tests/bahmni-poc-step1a.spec.ts)

---

## Verification Checklist (detail)

The sections below explain each check for reference. The scripts above run all of them automatically.

---

### Check 1: All containers are running

```bash
docker compose ps
```

**Pass:** All containers listed above are running (certs should be `exited 0`).
**Fail:** One or more containers are in `restarting` or `exited` with non-zero exit code → check logs: `docker compose logs <container-name>`

---

### Check 2: Database is healthy

```bash
# Note: docker compose exec uses SERVICE name, not container name
docker compose exec db.openelis.org pg_isready -d clinlims -U clinlims
```

**Pass:** Output: `/var/run/postgresql:5432 - accepting connections`
**Fail:** Connection refused → wait 30s and retry; if still failing, check `docker compose logs db.openelis.org`

---

### Check 3: external-fhir-api FHIR metadata endpoint responds

Access is via the nginx proxy at `https://localhost/fhir/` — nginx presents the shared self-signed cert to the FHIR store (mTLS).

```bash
curl -sk https://localhost/fhir/metadata | python3 -m json.tool | grep '"resourceType"'
```

**Pass:** Output contains `"resourceType": "CapabilityStatement"`
**Fail:** Connection refused / 502 → check `docker compose logs external-fhir-api`; HAPI FHIR takes ~30-60s to start. Also check `docker compose logs openelisglobal-proxy`.

---

### Check 4: external-fhir-api accepts a FHIR write (Patient POST)

This confirms the FHIR store accepts external writes — the mediator will need this in Phase 3.

```bash
curl -sk -X POST https://localhost/fhir/Patient \
  -H "Content-Type: application/fhir+json" \
  -d '{
    "resourceType": "Patient",
    "name": [{"family": "TestPatient", "given": ["PoC"]}],
    "birthDate": "1990-01-01",
    "gender": "male"
  }' | python3 -m json.tool | grep '"resourceType"'
```

**Pass:** Output contains `"resourceType": "Patient"` and an `"id"` field.
**Fail:** HTTP 4xx/5xx → check the full response body for the error message; `birthDate` and `gender` are required by OEG2 — both are included above.

**Save the returned Patient ID** — you'll need it in Step 1b.

```bash
# Run this version to capture the ID
curl -sk -X POST https://localhost/fhir/Patient \
  -H "Content-Type: application/fhir+json" \
  -d '{
    "resourceType": "Patient",
    "name": [{"family": "TestPatient", "given": ["PoC"]}],
    "birthDate": "1990-01-01",
    "gender": "male"
  }' | python3 -c "import sys,json; d=json.load(sys.stdin); print('Patient ID:', d.get('id','ERROR'))"
```

---

### Check 5: external-fhir-api accepts a FHIR read (GET Patient)

```bash
curl -sk "https://localhost/fhir/Patient?name=TestPatient" | python3 -m json.tool | grep '"total"'
```

**Pass:** `"total": 1` (or more if you ran Check 4 multiple times)
**Fail:** `"total": 0` → the POST in Check 4 failed silently; re-run Check 4 with `-v` flag

---

### Check 6: OEG2 web UI is accessible

Open in browser: **https://localhost** (accept the self-signed certificate warning)

**Pass:** OEG2 login page loads.
**Fail:** Browser cannot connect → check `docker compose logs openelisglobal-proxy`; check nginx logs

Login credentials: `admin` / `adminADMIN!`

---

### Check 7: OEG2 webapp API responds

```bash
curl -sk https://localhost/api/OpenELIS-Global/health
```

**Pass:** HTTP 200, response body `{"status":"UP"}`
**Fail:** 502/504 → webapp not yet ready; check `docker compose logs oe.openelis.org`

---

### Check 8: OEG2 is configured with correct FHIR polling URI

```bash
docker compose exec openelisglobal-webapp \
  grep "remote.source.uri" /run/secrets/common.properties
```

**Pass:** `org.openelisglobal.remote.source.uri=https://fhir.openelis.org:8443/fhir/`
**Fail:** Empty value → the common.properties changes in Setup step 1 were not applied; restart the webapp: `docker compose restart openelisglobal-webapp`

---

### Check 9: OEG2 logs show FHIR polling activity

```bash
docker compose logs openelisglobal-webapp 2>&1 | grep -i "fhir\|task\|poll\|remote" | tail -30
```

**Pass:** Log lines containing references to FHIR Task polling, even if no Tasks are found yet. Look for lines like:
- `Searching for tasks from remote source`
- `No tasks found`
- References to `FhirApiWorkFlowServiceImpl`

**Fail (warning, not a blocker):** No relevant log lines → wait 20s (the configured poll frequency) and re-run; OEG2 may not log anything until the first poll cycle completes

---

### Check 10: Confirm external-fhir-api is reachable from OEG2 webapp container

This confirms Docker internal networking works — from inside the webapp container, the nginx proxy must be reachable, which in turn reaches external-fhir-api with mTLS.

```bash
# Note: service name is 'oe.openelis.org' and Docker-internal DNS uses service name 'proxy'
docker compose exec oe.openelis.org \
  curl -sk https://proxy/fhir/metadata | grep '"resourceType"'
```

**Pass:** `"resourceType":"CapabilityStatement"`
**Fail:** Connection refused or DNS resolution failure → check `docker compose ps`, `docker network ls`, and `docker compose logs proxy`

---

## Summary: Pass Criteria

Step 1a is **complete** when all of the following are true:

- [x] All 6 containers running (Check 1, 2)
- [x] `external-fhir-api` responds to FHIR reads and writes from localhost (Checks 3, 4, 5)
- [x] OEG2 web UI accessible and login works (Check 6, 7)
- [x] OEG2 is configured with `remote.source.uri` pointing to external-fhir-api (Check 8)
- [x] OEG2 webapp can reach external-fhir-api via internal Docker DNS (Check 10)

Check 9 (poll log activity) is informational — a warning if absent but not a blocker for Step 1a.

---

## Stopping / Cleanup

```bash
# Stop all containers (keeps data volumes)
docker compose stop

# Stop and remove containers + networks (keeps data volumes)
docker compose down

# Full teardown including volumes (destroys database)
docker compose down -v
```

---

## Next Step

Once you confirm the checklist above is complete: **[Phase 1, Step 1b — Validate End-to-End FHIR Flow](phase1-step1b-validate-fhir-flow.md)**
