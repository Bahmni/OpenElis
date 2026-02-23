#!/usr/bin/env bash
# =============================================================================
# Phase 1, Step 1b — Validate End-to-End FHIR Flow Demo Script
# =============================================================================
# Single entry point for the SME demo.
# Pushes FHIR resources, waits for OEG2 to pick up the order, runs Playwright
# UI tests for result entry + validation, then verifies the full Task lifecycle.
#
# Usage (from anywhere):
#   bash /Users/vishalkarmalkar/IdeaProjects/bahmni/OpenElis/docs/steps/scripts/phase1-step1b-demo.sh
#
# Prerequisites:
#   - OEG2 containers running (Step 1a)
#   - Node.js + Playwright installed
# =============================================================================

set -uo pipefail

# ── Config ────────────────────────────────────────────────────────────────────
OEG2_DIR="/Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/OpenELIS-Global-2"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
VERIFY_SCRIPT="$SCRIPT_DIR/phase1-step1b-verify.sh"
STATE_FILE="$SCRIPT_DIR/.step1b-state.env"
FHIR_BASE="https://localhost/fhir"

# ── Colour helpers ─────────────────────────────────────────────────────────────
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
BOLD='\033[1m'
RESET='\033[0m'

step()  { echo -e "\n${BOLD}${CYAN}▶  $1${RESET}"; }
info()  { echo -e "   $1"; }
ok()    { echo -e "   ${GREEN}✓ $1${RESET}"; }
err()   { echo -e "   ${RED}✗ $1${RESET}"; }
warn()  { echo -e "   ${YELLOW}⚠ $1${RESET}"; }

# ── Banner ────────────────────────────────────────────────────────────────────
echo ""
echo -e "${BOLD}╔══════════════════════════════════════════════════════════════╗${RESET}"
echo -e "${BOLD}║  Bahmni + OE-Global-2 Integration — Phase 1 / Step 1b Demo  ║${RESET}"
echo -e "${BOLD}╚══════════════════════════════════════════════════════════════╝${RESET}"
echo -e "  Date: $(date)"
echo -e "  OEG2: $OEG2_DIR"

# ── Step 0/6: Demo DB setup (idempotent) ────────────────────────────────────
step "Step 0/6: Demo DB setup (idempotent)"

# Disable National ID requirement (demo DB has it enabled, our FHIR Patient lacks one)
NATID_CURRENT=$(docker exec openelisglobal-database psql -U clinlims clinlims -t -A \
  -c "SELECT value FROM clinlims.site_information WHERE name = 'National ID required';" 2>/dev/null || echo "unknown")
if [[ "$NATID_CURRENT" == "true" ]]; then
  docker exec openelisglobal-database psql -U clinlims clinlims \
    -c "UPDATE clinlims.site_information SET value = 'false' WHERE name = 'National ID required';" 2>/dev/null
  ok "Disabled 'National ID required' (was: true)"
  DB_CHANGED=true
else
  ok "National ID requirement already disabled"
  DB_CHANGED=false
fi

# Create organization "Bahmni Clinic" as a referring clinic (type 5) if not exists
ORG_EXISTS=$(docker exec openelisglobal-database psql -U clinlims clinlims -t -A \
  -c "SELECT count(*) FROM clinlims.organization WHERE name = 'Bahmni Clinic';" 2>/dev/null || echo "0")
if [[ "$ORG_EXISTS" == "0" ]]; then
  docker exec openelisglobal-database psql -U clinlims clinlims -c "
    INSERT INTO clinlims.organization (id, name, is_active, mls_sentinel_lab_flag)
    VALUES (nextval('clinlims.organization_seq'), 'Bahmni Clinic', 'Y', 'N');
    INSERT INTO clinlims.organization_organization_type (org_id, org_type_id)
    VALUES (currval('clinlims.organization_seq'), 5);
  " 2>/dev/null
  ok "Created organization 'Bahmni Clinic' (referring clinic)"
  DB_CHANGED=true
else
  ok "Organization 'Bahmni Clinic' already exists"
fi

# Restart webapp if DB was changed (to refresh site_information cache)
if [[ "$DB_CHANGED" == "true" ]]; then
  info "Restarting webapp to refresh config cache..."
  docker restart openelisglobal-webapp >/dev/null 2>&1
  # Wait for webapp to come back up
  WEBAPP_WAIT=0
  while [[ $WEBAPP_WAIT -lt 90 ]]; do
    HEALTH=$(curl -sk "https://localhost/api/OpenELIS-Global/health" 2>/dev/null \
      | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('status',''))" 2>/dev/null || echo "")
    if [[ "$HEALTH" == "UP" ]]; then
      break
    fi
    echo -ne "\r   Waiting for webapp... ${WEBAPP_WAIT}s/90s"
    sleep 5
    WEBAPP_WAIT=$((WEBAPP_WAIT + 5))
  done
  echo ""
  if [[ "$HEALTH" == "UP" ]]; then
    ok "Webapp restarted and healthy"
  else
    err "Webapp not healthy after 90s — check docker logs"
    exit 1
  fi
fi

# ── Step 1/6: Pre-flight checks ──────────────────────────────────────────────
step "Step 1/6: Pre-flight checks"

# Check containers
EXPECTED_CONTAINERS=("openelisglobal-database" "external-fhir-api" "openelisglobal-webapp"
                     "openelisglobal-front-end" "openelisglobal-proxy")
ALL_RUNNING=true
for c in "${EXPECTED_CONTAINERS[@]}"; do
  STATE=$(docker inspect --format '{{.State.Status}}' "$c" 2>/dev/null || echo "missing")
  if [[ "$STATE" != "running" ]]; then
    err "$c — state: $STATE"
    ALL_RUNNING=false
  fi
done

if [[ "$ALL_RUNNING" == "false" ]]; then
  err "One or more containers not running. Run Step 1a demo first."
  exit 1
fi
ok "All containers running"

# Check FHIR store
FHIR_META=$(curl -sk "$FHIR_BASE/metadata" 2>/dev/null \
  | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('resourceType',''))" 2>/dev/null || echo "")
if [[ "$FHIR_META" != "CapabilityStatement" ]]; then
  err "FHIR store not reachable at $FHIR_BASE/metadata"
  exit 1
fi
ok "FHIR store reachable"

# Check webapp health
HEALTH=$(curl -sk "https://localhost/api/OpenELIS-Global/health" 2>/dev/null \
  | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('status',''))" 2>/dev/null || echo "")
if [[ "$HEALTH" != "UP" ]]; then
  err "OEG2 webapp not healthy"
  exit 1
fi
ok "OEG2 webapp healthy"

# ── Step 2/6: Push FHIR resources ────────────────────────────────────────────
step "Step 2/6: Push FHIR resources to external-fhir-api"

AUTHORED_ON=$(date -u +"%Y-%m-%dT%H:%M:%SZ")

# OEG2 requires UUID-format IDs (FhirApiWorkFlowServiceImpl.java line 560
# calls UUID.fromString on Practitioner ID). HAPI FHIR's default POST gives
# integer IDs, so we use PUT with client-assigned UUIDs.
gen_uuid() {
  python3 -c "import uuid; print(uuid.uuid4())"
}

fhir_put() {
  local resource_type="$1"
  local id="$2"
  local json_body="$3"
  local response
  response=$(curl -sk -X PUT "$FHIR_BASE/$resource_type/$id" \
    -H "Content-Type: application/fhir+json" \
    -d "$json_body" 2>/dev/null)
  echo "$response" | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('id',''))" 2>/dev/null || echo ""
}

# Pre-generate UUIDs
PRACTITIONER_ID=$(gen_uuid)
PATIENT_ID=$(gen_uuid)
SR_ID=$(gen_uuid)
TASK_ID=$(gen_uuid)

# PUT Practitioner (with UUID)
info "Creating Practitioner (UUID: $PRACTITIONER_ID)..."
PRACT_RESULT=$(fhir_put "Practitioner" "$PRACTITIONER_ID" "{
  \"resourceType\": \"Practitioner\",
  \"id\": \"$PRACTITIONER_ID\",
  \"name\": [{\"family\": \"BahmniMediatorPractitioner\", \"given\": [\"PoC\"]}],
  \"active\": true
}")
if [[ -z "$PRACT_RESULT" ]]; then
  err "Practitioner PUT failed"
  exit 1
fi
ok "Practitioner created — ID: $PRACTITIONER_ID"

# PUT Patient (with UUID)
info "Creating Patient (UUID: $PATIENT_ID)..."
PAT_RESULT=$(fhir_put "Patient" "$PATIENT_ID" "{
  \"resourceType\": \"Patient\",
  \"id\": \"$PATIENT_ID\",
  \"identifier\": [{\"system\": \"http://openelis-global.org/pat_subjectNumber\", \"value\": \"POC-PAT-001\"}],
  \"name\": [{\"family\": \"TestPatient\", \"given\": [\"Bahmni\"]}],
  \"birthDate\": \"1990-06-15\",
  \"gender\": \"male\"
}")
if [[ -z "$PAT_RESULT" ]]; then
  err "Patient PUT failed"
  exit 1
fi
ok "Patient created — ID: $PATIENT_ID"

# PUT ServiceRequest (with UUID)
info "Creating ServiceRequest (LOINC 736-9, UUID: $SR_ID)..."
SR_RESULT=$(fhir_put "ServiceRequest" "$SR_ID" "{
  \"resourceType\": \"ServiceRequest\",
  \"id\": \"$SR_ID\",
  \"status\": \"active\",
  \"intent\": \"order\",
  \"subject\": {\"reference\": \"Patient/$PATIENT_ID\"},
  \"code\": {
    \"coding\": [{
      \"system\": \"http://loinc.org\",
      \"code\": \"736-9\",
      \"display\": \"Lymphocytes [#/volume] in Blood by Manual count\"
    }]
  },
  \"identifier\": [{\"value\": \"POC-LAB-ORDER-001\"}]
}")
if [[ -z "$SR_RESULT" ]]; then
  err "ServiceRequest PUT failed"
  exit 1
fi
ok "ServiceRequest created — ID: $SR_ID"

# PUT Task (with UUID + pre-included identifier to avoid OEG2 version conflict)
info "Creating Task (status=requested, owner=Practitioner/$PRACTITIONER_ID, UUID: $TASK_ID)..."
TASK_RESULT=$(fhir_put "Task" "$TASK_ID" "{
  \"resourceType\": \"Task\",
  \"id\": \"$TASK_ID\",
  \"identifier\": [{
    \"type\": {\"coding\": [{\"system\": \"http://openelis-global.org/genIdType\", \"code\": \"externalId\"}]},
    \"system\": \"https://fhir.openelis.org:8443/fhir/\",
    \"value\": \"$TASK_ID\"
  }],
  \"status\": \"requested\",
  \"intent\": \"order\",
  \"basedOn\": [{\"reference\": \"ServiceRequest/$SR_ID\"}],
  \"for\": {\"reference\": \"Patient/$PATIENT_ID\"},
  \"owner\": {\"reference\": \"Practitioner/$PRACTITIONER_ID\"},
  \"authoredOn\": \"$AUTHORED_ON\"
}")
if [[ -z "$TASK_RESULT" ]]; then
  err "Task PUT failed"
  exit 1
fi
ok "Task created — ID: $TASK_ID"

# Save state
cat > "$STATE_FILE" <<EOF
# Step 1b state — generated $(date)
PRACTITIONER_ID=$PRACTITIONER_ID
PATIENT_ID=$PATIENT_ID
SR_ID=$SR_ID
TASK_ID=$TASK_ID
AUTHORED_ON=$AUTHORED_ON
EOF
info "State saved to $STATE_FILE"

# ── Step 3/6: Wait for OEG2 to pick up the order ─────────────────────────────
step "Step 3/6: Wait for OEG2 to pick up the Task"
info "Polling Task status every 10s (timeout: 120s)..."
info "OEG2 polls external-fhir-api every 20s for new Tasks"

TASK_STATUS="requested"
MAX_WAIT=120
ELAPSED=0
while [[ $ELAPSED -lt $MAX_WAIT ]]; do
  TASK_STATUS=$(curl -sk "$FHIR_BASE/Task/$TASK_ID" 2>/dev/null \
    | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('status',''))" 2>/dev/null || echo "unknown")
  if [[ "$TASK_STATUS" != "requested" && "$TASK_STATUS" != "unknown" ]]; then
    break
  fi
  echo -ne "\r   Waiting... ${ELAPSED}s/${MAX_WAIT}s (Task status: $TASK_STATUS)"
  sleep 10
  ELAPSED=$((ELAPSED + 10))
done
echo ""

if [[ "$TASK_STATUS" != "requested" && "$TASK_STATUS" != "unknown" ]]; then
  ok "Task picked up — status: $TASK_STATUS"
else
  err "Task still '$TASK_STATUS' after ${MAX_WAIT}s — OEG2 did not pick up the order"
  info "Check: docker compose logs oe.openelis.org | grep -i 'task\\|fhir\\|remote' | tail -30"
  info "Verify: common.properties has remote.source.identifier=Practitioner/*"
  exit 1
fi

# ── Step 4/6: Run Playwright UI tests ─────────────────────────────────────────
step "Step 4/6: Run Playwright UI tests (result entry + validation)"
echo ""

PLAYWRIGHT_EXIT=0
if command -v npx &>/dev/null; then
  (
    cd "$OEG2_DIR/frontend"
    TASK_ID="$TASK_ID" SR_ID="$SR_ID" PATIENT_ID="$PATIENT_ID" \
      PRACTITIONER_ID="$PRACTITIONER_ID" \
      TEST_USER=admin TEST_PASS="adminADMIN!" \
      npx playwright test bahmni-poc-step1b --reporter=list 2>&1
  )
  PLAYWRIGHT_EXIT=$?
  echo ""
  if [[ "$PLAYWRIGHT_EXIT" -eq 0 ]]; then
    ok "Playwright UI tests passed"
  else
    err "Playwright UI tests failed (exit code $PLAYWRIGHT_EXIT)"
  fi
else
  warn "npx not found — skipping Playwright tests"
  info "Install Node.js and run: cd $OEG2_DIR/frontend && npx playwright install chromium"
  info ""
  info "Manual steps required:"
  info "  1. Go to https://localhost → Order → Incoming Orders"
  info "  2. Search for POC-LAB-ORDER-001 → click to create order"
  info "  3. Go to Results → By Order → enter accession number → enter result → Save"
  info "  4. Go to Validation → By Order → enter accession number → Accept → Save"
  PLAYWRIGHT_EXIT=0  # Don't fail demo if npx is missing
fi

# ── Step 5/6: Wait for DiagnosticReport ───────────────────────────────────────
step "Step 5/6: Check for DiagnosticReport in FHIR store"

DR_STATUS=""
DR_TOTAL=0
MAX_WAIT=180
ELAPSED=0
info "Polling for DiagnosticReport (timeout: ${MAX_WAIT}s)..."
while [[ $ELAPSED -lt $MAX_WAIT ]]; do
  DR_SEARCH=$(curl -sk "$FHIR_BASE/DiagnosticReport?based-on=ServiceRequest/$SR_ID" 2>/dev/null)
  DR_TOTAL=$(echo "$DR_SEARCH" | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('total',0))" 2>/dev/null || echo "0")
  if [[ "$DR_TOTAL" -ge 1 ]]; then
    DR_STATUS=$(echo "$DR_SEARCH" | python3 -c "
import sys,json
d=json.load(sys.stdin)
entries=d.get('entry',[])
if entries:
    print(entries[0].get('resource',{}).get('status','unknown'))
else:
    print('unknown')
" 2>/dev/null || echo "unknown")
    if [[ "$DR_STATUS" == "final" ]]; then
      break
    fi
  fi
  echo -ne "\r   Waiting... ${ELAPSED}s/${MAX_WAIT}s (DiagnosticReports found: $DR_TOTAL)"
  sleep 10
  ELAPSED=$((ELAPSED + 10))
done
echo ""

if [[ "$DR_TOTAL" -ge 1 ]]; then
  if [[ "$DR_STATUS" == "final" ]]; then
    ok "DiagnosticReport found — status: final"
  else
    warn "DiagnosticReport found — status: $DR_STATUS (expected: final)"
  fi
else
  warn "No DiagnosticReport found after ${MAX_WAIT}s"
  info "This is expected if result entry was skipped (no Playwright)"
fi

# ── Step 6/6: Verify Task lifecycle ───────────────────────────────────────────
step "Step 6/6: Verify Task lifecycle"

FINAL_TASK_STATUS=$(curl -sk "$FHIR_BASE/Task/$TASK_ID" 2>/dev/null \
  | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('status',''))" 2>/dev/null || echo "unknown")

info "Task lifecycle (externally visible on external-fhir-api):"
if [[ "$FINAL_TASK_STATUS" == "completed" ]]; then
  ok "requested → accepted → completed"
elif [[ "$FINAL_TASK_STATUS" == "accepted" ]]; then
  info "requested → accepted → (awaiting result validation)"
else
  info "Current status: $FINAL_TASK_STATUS"
fi

# ── Summary ───────────────────────────────────────────────────────────────────
echo ""
echo -e "${BOLD}══════════════════════════════════════════════════════════════${RESET}"
echo -e "${BOLD}  Final Summary${RESET}"
echo -e "${BOLD}══════════════════════════════════════════════════════════════${RESET}"
echo ""
echo -e "  Resource IDs:"
echo -e "    Practitioner: ${CYAN}$PRACTITIONER_ID${RESET}"
echo -e "    Patient:      ${CYAN}$PATIENT_ID${RESET}"
echo -e "    ServiceReq:   ${CYAN}$SR_ID${RESET}"
echo -e "    Task:         ${CYAN}$TASK_ID${RESET}"
echo ""
echo -e "  Task lifecycle: ${CYAN}$FINAL_TASK_STATUS${RESET}"
echo -e "  DiagnosticReport: ${CYAN}${DR_STATUS:-not found}${RESET}"
echo ""

OVERALL_EXIT=0

if [[ "$FINAL_TASK_STATUS" == "completed" && "$DR_STATUS" == "final" ]]; then
  echo -e "${GREEN}${BOLD}  ✓ Step 1b COMPLETE — full end-to-end FHIR flow validated!${RESET}"
elif [[ "$FINAL_TASK_STATUS" == "accepted" ]]; then
  echo -e "${YELLOW}${BOLD}  ◐ Step 1b PARTIAL — Task accepted, awaiting result entry + validation.${RESET}"
  if [[ "$PLAYWRIGHT_EXIT" -ne 0 ]]; then
    OVERALL_EXIT=1
  fi
else
  echo -e "${RED}${BOLD}  ✗ Step 1b INCOMPLETE — see output above.${RESET}"
  OVERALL_EXIT=1
fi

echo ""
echo -e "  OEG2 web UI: ${CYAN}https://localhost${RESET}"
echo -e "  Login: ${CYAN}admin / adminADMIN!${RESET}"
echo -e "  FHIR store: ${CYAN}https://localhost/fhir/${RESET}"
echo ""
exit "$OVERALL_EXIT"
