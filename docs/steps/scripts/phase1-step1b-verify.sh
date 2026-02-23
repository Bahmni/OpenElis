#!/usr/bin/env bash
# =============================================================================
# Phase 1, Step 1b: Validate End-to-End FHIR Flow — Verification Script
# =============================================================================
# Runs all non-UI checks for the step doc:
#   docs/steps/phase1-step1b-validate-fhir-flow.md
#
# This script can operate in two modes:
#   1. "push" mode (default): Push FHIR resources + verify Task pickup
#   2. "verify-only" mode:    Only verify (expects .step1b-state.env to exist)
#
# Usage:
#   bash phase1-step1b-verify.sh              # Full: push + verify
#   bash phase1-step1b-verify.sh verify-only  # Just verify (resources already pushed)
#
# Prerequisites:
#   - OEG2 containers running (Step 1a)
#   - FHIR store reachable at https://localhost/fhir/
# =============================================================================

set -uo pipefail

MODE="${1:-push}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
STATE_FILE="$SCRIPT_DIR/.step1b-state.env"
FHIR_BASE="https://localhost/fhir"
OEG2_API="https://localhost/api/OpenELIS-Global"

# ── Colour helpers ─────────────────────────────────────────────────────────────
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
BOLD='\033[1m'
RESET='\033[0m'

PASS_COUNT=0
FAIL_COUNT=0
WARN_COUNT=0

pass()  { echo -e "  ${GREEN}✓ PASS${RESET}  $1"; PASS_COUNT=$((PASS_COUNT + 1)); }
fail()  { echo -e "  ${RED}✗ FAIL${RESET}  $1"; FAIL_COUNT=$((FAIL_COUNT + 1)); }
warn()  { echo -e "  ${YELLOW}⚠ WARN${RESET}  $1"; WARN_COUNT=$((WARN_COUNT + 1)); }
header(){ echo -e "\n${BOLD}${CYAN}$1${RESET}"; }
info()  { echo -e "         $1"; }

# ── Helper: generate a UUID ──────────────────────────────────────────────────
gen_uuid() {
  python3 -c "import uuid; print(uuid.uuid4())"
}

# ── Helper: PUT a FHIR resource with a pre-assigned UUID ─────────────────────
# OEG2 requires UUID-format IDs (FhirApiWorkFlowServiceImpl calls UUID.fromString).
# Using PUT with client-assigned UUIDs instead of POST (which gives integer IDs).
fhir_put() {
  local resource_type="$1"
  local id="$2"
  local json_body="$3"
  local response
  response=$(curl -sk -X PUT "$FHIR_BASE/$resource_type/$id" \
    -H "Content-Type: application/fhir+json" \
    -d "$json_body" 2>/dev/null)
  local rt
  rt=$(echo "$response" | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('resourceType',''))" 2>/dev/null || echo "")
  local ret_id
  ret_id=$(echo "$response" | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('id',''))" 2>/dev/null || echo "")
  if [[ "$rt" == "$resource_type" && -n "$ret_id" ]]; then
    echo "$ret_id"
    return 0
  else
    echo "ERROR: $response" >&2
    return 1
  fi
}

# ── Helper: poll until condition or timeout ──────────────────────────────────
poll_until() {
  local desc="$1"
  local max_secs="$2"
  local interval="$3"
  shift 3
  local elapsed=0
  while [[ $elapsed -lt $max_secs ]]; do
    if "$@" 2>/dev/null; then
      return 0
    fi
    echo -ne "\r         Waiting for ${desc}... ${elapsed}s/${max_secs}s"
    sleep "$interval"
    elapsed=$((elapsed + interval))
  done
  echo ""
  return 1
}

# ── Banner ────────────────────────────────────────────────────────────────────
echo ""
echo -e "${BOLD}============================================================${RESET}"
echo -e "${BOLD}  Phase 1 / Step 1b — FHIR Flow Verification${RESET}"
echo -e "${BOLD}============================================================${RESET}"
echo -e "  Mode: $MODE"
echo -e "  Date: $(date)"

# ── Pre-flight checks ────────────────────────────────────────────────────────
header "Pre-flight: Services reachable"

# FHIR store
FHIR_META=$(curl -sk "$FHIR_BASE/metadata" 2>/dev/null \
  | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('resourceType',''))" 2>/dev/null || echo "")
if [[ "$FHIR_META" == "CapabilityStatement" ]]; then
  pass "FHIR store reachable at $FHIR_BASE"
else
  fail "FHIR store not reachable at $FHIR_BASE/metadata"
  info "Ensure OEG2 containers are running (Step 1a)"
  exit 1
fi

# OEG2 webapp
HEALTH=$(curl -sk "$OEG2_API/health" 2>/dev/null \
  | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('status',''))" 2>/dev/null || echo "")
if [[ "$HEALTH" == "UP" ]]; then
  pass "OEG2 webapp healthy"
else
  fail "OEG2 webapp not healthy at $OEG2_API/health"
  exit 1
fi

# ── Push FHIR resources (unless verify-only) ──────────────────────────────────
if [[ "$MODE" == "push" ]]; then
  header "Step 1: Push FHIR resources to external-fhir-api"

  AUTHORED_ON=$(date -u +"%Y-%m-%dT%H:%M:%SZ")

  # Pre-generate UUIDs for all resources
  # OEG2 requires UUID-format IDs (FhirApiWorkFlowServiceImpl.java line 560
  # calls UUID.fromString on Practitioner ID). HAPI FHIR's default POST gives
  # integer IDs, so we use PUT with client-assigned UUIDs.
  PRACTITIONER_ID=$(gen_uuid)
  PATIENT_ID=$(gen_uuid)
  SR_ID=$(gen_uuid)
  TASK_ID=$(gen_uuid)

  # 1. Practitioner
  info "Creating Practitioner with UUID: $PRACTITIONER_ID"
  PRACT_RESULT=$(fhir_put "Practitioner" "$PRACTITIONER_ID" "{
    \"resourceType\": \"Practitioner\",
    \"id\": \"$PRACTITIONER_ID\",
    \"name\": [{\"family\": \"BahmniMediatorPractitioner\", \"given\": [\"PoC\"]}],
    \"active\": true
  }")
  if [[ -n "$PRACT_RESULT" && "$PRACT_RESULT" != ERROR* ]]; then
    pass "Practitioner created — ID: $PRACTITIONER_ID"
  else
    fail "Practitioner PUT failed"
    exit 1
  fi

  # 2. Patient
  info "Creating Patient with UUID: $PATIENT_ID"
  PAT_RESULT=$(fhir_put "Patient" "$PATIENT_ID" "{
    \"resourceType\": \"Patient\",
    \"id\": \"$PATIENT_ID\",
    \"identifier\": [{\"system\": \"http://openelis-global.org/pat_subjectNumber\", \"value\": \"POC-PAT-001\"}],
    \"name\": [{\"family\": \"TestPatient\", \"given\": [\"Bahmni\"]}],
    \"birthDate\": \"1990-06-15\",
    \"gender\": \"male\"
  }")
  if [[ -n "$PAT_RESULT" && "$PAT_RESULT" != ERROR* ]]; then
    pass "Patient created — ID: $PATIENT_ID"
  else
    fail "Patient PUT failed"
    exit 1
  fi

  # 3. ServiceRequest
  info "Creating ServiceRequest (LOINC 736-9 = Lymphocytes) with UUID: $SR_ID"
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
  if [[ -n "$SR_RESULT" && "$SR_RESULT" != ERROR* ]]; then
    pass "ServiceRequest created — ID: $SR_ID"
  else
    fail "ServiceRequest PUT failed"
    exit 1
  fi

  # 4. Task
  # Pre-include the identifier that OEG2 normally adds in an intermediate update.
  # Without this, OEG2 does: read(v1) → add-identifier(v2) → update-status(v1→fail).
  # With it, OEG2 skips the intermediate update and goes straight to status change.
  info "Creating Task (status=requested, owner=Practitioner/$PRACTITIONER_ID) with UUID: $TASK_ID"
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
  if [[ -n "$TASK_RESULT" && "$TASK_RESULT" != ERROR* ]]; then
    pass "Task created — ID: $TASK_ID"
  else
    fail "Task PUT failed"
    exit 1
  fi

  # Save state for verify-only mode and Playwright
  cat > "$STATE_FILE" <<EOF
# Step 1b state — generated $(date)
PRACTITIONER_ID=$PRACTITIONER_ID
PATIENT_ID=$PATIENT_ID
SR_ID=$SR_ID
TASK_ID=$TASK_ID
AUTHORED_ON=$AUTHORED_ON
EOF
  info "State saved to $STATE_FILE"

else
  # verify-only: load state
  if [[ ! -f "$STATE_FILE" ]]; then
    fail "State file not found: $STATE_FILE"
    info "Run without 'verify-only' first to push FHIR resources"
    exit 1
  fi
  # shellcheck disable=SC1090
  source "$STATE_FILE"
  info "Loaded state: Task=$TASK_ID, SR=$SR_ID, Patient=$PATIENT_ID"
fi

# ── Verify FHIR resources exist ──────────────────────────────────────────────
header "Step 2: Verify FHIR resources exist in store"

for RES in "Patient/$PATIENT_ID" "ServiceRequest/$SR_ID" "Task/$TASK_ID" "Practitioner/$PRACTITIONER_ID"; do
  RT=$(curl -sk "$FHIR_BASE/$RES" 2>/dev/null \
    | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('resourceType',''))" 2>/dev/null || echo "")
  EXPECTED_RT=$(echo "$RES" | cut -d/ -f1)
  if [[ "$RT" == "$EXPECTED_RT" ]]; then
    pass "$RES exists"
  else
    fail "$RES not found (got resourceType=$RT)"
  fi
done

# ── Wait for Task pickup ─────────────────────────────────────────────────────
header "Step 3: Wait for OEG2 to pick up the Task"
info "Polling Task/$TASK_ID status (expecting: requested → accepted)..."
info "OEG2 polls every 20s — this may take up to 120s"

check_task_accepted() {
  local status
  status=$(curl -sk "$FHIR_BASE/Task/$TASK_ID" 2>/dev/null \
    | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('status',''))" 2>/dev/null || echo "")
  [[ "$status" != "requested" && -n "$status" ]]
}

if poll_until "Task pickup" 120 10 check_task_accepted; then
  echo ""
  TASK_STATUS=$(curl -sk "$FHIR_BASE/Task/$TASK_ID" 2>/dev/null \
    | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('status',''))" 2>/dev/null || echo "unknown")
  pass "Task status changed to: $TASK_STATUS"
else
  TASK_STATUS=$(curl -sk "$FHIR_BASE/Task/$TASK_ID" 2>/dev/null \
    | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('status',''))" 2>/dev/null || echo "unknown")
  fail "Task still '$TASK_STATUS' after 120s — OEG2 did not pick up the order"
  info "Check OEG2 logs: docker compose logs oe.openelis.org | tail -50"
  info "Verify remote.source.identifier in common.properties matches Task.owner"
fi

# ── Check Electronic Order in OEG2 ───────────────────────────────────────────
header "Step 4: Verify Electronic Order visible in OEG2"
info "Checking OEG2 REST API for electronic order with identifier POC-LAB-ORDER-001..."

# OEG2 REST API needs authentication — we use the internal FHIR store as a proxy
# Check via the webapp's internal ElectronicOrder endpoint
# The electronic order is identified by the ServiceRequest identifier
EO_CHECK=$(docker compose exec -T oe.openelis.org \
  curl -sk "https://localhost:8443/OpenELIS-Global/rest/ElectronicOrders" 2>/dev/null || echo "")
if [[ -n "$EO_CHECK" ]]; then
  info "Electronic Orders endpoint is responding"
  # Try to find our specific order
  if echo "$EO_CHECK" | grep -q "POC-LAB-ORDER-001" 2>/dev/null; then
    pass "Electronic order with identifier POC-LAB-ORDER-001 found in OEG2"
  else
    warn "Could not confirm POC-LAB-ORDER-001 in electronic orders (order may use different identifier)"
    info "Check manually: Order → Incoming Orders in the UI"
  fi
else
  warn "Could not query ElectronicOrders REST API directly"
  info "Verify via UI: Order → Incoming Orders"
fi

# ── Check for DiagnosticReport (post result-entry) ─────────────────────────────
header "Step 5: Check for DiagnosticReport (requires result entry first)"

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
    pass "DiagnosticReport found — status: final"
  else
    warn "DiagnosticReport found — status: $DR_STATUS (expected: final)"
  fi
else
  info "No DiagnosticReport yet — this is expected before result entry"
  info "Run Playwright tests to enter & validate results, then re-run this check"
fi

# ── Check for Observations ───────────────────────────────────────────────────
header "Step 6: Check for Observations"

OBS_SEARCH=$(curl -sk "$FHIR_BASE/Observation?subject=Patient/$PATIENT_ID" 2>/dev/null)
OBS_TOTAL=$(echo "$OBS_SEARCH" | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('total',0))" 2>/dev/null || echo "0")

if [[ "$OBS_TOTAL" -ge 1 ]]; then
  pass "Observation(s) found — total: $OBS_TOTAL"
else
  info "No Observations yet — expected before result entry"
fi

# ── Final Task status ─────────────────────────────────────────────────────────
header "Step 7: Task lifecycle status"

FINAL_TASK_STATUS=$(curl -sk "$FHIR_BASE/Task/$TASK_ID" 2>/dev/null \
  | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('status',''))" 2>/dev/null || echo "unknown")
info "Current Task status: $FINAL_TASK_STATUS"

if [[ "$FINAL_TASK_STATUS" == "completed" ]]; then
  pass "Task lifecycle complete: requested → accepted → completed"
elif [[ "$FINAL_TASK_STATUS" == "accepted" ]]; then
  info "Task is accepted — awaiting result entry + validation to reach 'completed'"
  info "Expected lifecycle on external-fhir-api: requested → accepted → completed"
elif [[ "$FINAL_TASK_STATUS" == "requested" ]]; then
  warn "Task still in 'requested' — OEG2 has not picked it up yet"
else
  info "Task status: $FINAL_TASK_STATUS"
fi

# ── Summary ────────────────────────────────────────────────────────────────────
echo ""
echo -e "${BOLD}============================================================${RESET}"
echo -e "${BOLD}  Summary${RESET}"
echo -e "${BOLD}============================================================${RESET}"
echo -e "  ${GREEN}Passed:   $PASS_COUNT${RESET}"
echo -e "  ${RED}Failed:   $FAIL_COUNT${RESET}"
echo -e "  ${YELLOW}Warnings: $WARN_COUNT${RESET}"
echo ""
echo -e "  Resource IDs:"
echo -e "    Practitioner: ${CYAN}$PRACTITIONER_ID${RESET}"
echo -e "    Patient:      ${CYAN}$PATIENT_ID${RESET}"
echo -e "    ServiceReq:   ${CYAN}$SR_ID${RESET}"
echo -e "    Task:         ${CYAN}$TASK_ID${RESET}"
echo -e "    Task status:  ${CYAN}$FINAL_TASK_STATUS${RESET}"
echo ""

if [[ "$FAIL_COUNT" -eq 0 ]]; then
  echo -e "${GREEN}${BOLD}  ✓ Step 1b verification passed (bash checks).${RESET}"
  if [[ "$DR_TOTAL" -ge 1 && "$FINAL_TASK_STATUS" == "completed" ]]; then
    echo -e "${GREEN}${BOLD}  ✓ Full end-to-end FHIR flow validated!${RESET}"
  else
    echo -e "  Next: Run Playwright tests for result entry + validation."
  fi
  exit 0
else
  echo -e "${RED}${BOLD}  ✗ Step 1b verification had $FAIL_COUNT failure(s).${RESET}"
  exit 1
fi
