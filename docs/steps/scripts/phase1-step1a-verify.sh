#!/usr/bin/env bash
# =============================================================================
# Phase 1, Step 1a: OE-Global-2 Spin-Up Verification
# =============================================================================
# Runs all non-UI checks for the step doc:
#   docs/steps/phase1-step1a-spin-up-oeg2.md
#
# Usage:
#   cd /Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/OpenELIS-Global-2
#   bash /Users/vishalkarmalkar/IdeaProjects/bahmni/OpenElis/docs/steps/scripts/phase1-step1a-verify.sh
#
# Prerequisites: docker compose must be up. Run phase1-step1a-demo.sh to start
#               containers and run this automatically.
# =============================================================================

# No set -e: this is a verification script that must continue past failures
set -uo pipefail

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
PATIENT_ID=""

pass()  { echo -e "  ${GREEN}✓ PASS${RESET}  $1"; PASS_COUNT=$((PASS_COUNT + 1)); }
fail()  { echo -e "  ${RED}✗ FAIL${RESET}  $1"; FAIL_COUNT=$((FAIL_COUNT + 1)); }
warn()  { echo -e "  ${YELLOW}⚠ WARN${RESET}  $1"; WARN_COUNT=$((WARN_COUNT + 1)); }
header(){ echo -e "\n${BOLD}${CYAN}$1${RESET}"; }
info()  { echo -e "         $1"; }

# ── Helper: wait for a command to succeed with retries ──────────────────────
wait_for() {
  local desc="$1"
  local max_attempts="$2"
  local sleep_secs="$3"
  shift 3
  local cmd=("$@")

  for ((i=1; i<=max_attempts; i++)); do
    if "${cmd[@]}" &>/dev/null; then
      return 0
    fi
    echo -e "         Waiting for ${desc}... attempt ${i}/${max_attempts}"
    sleep "$sleep_secs"
  done
  return 1
}

# ── Helper: get container state via docker inspect ───────────────────────────
container_state() {
  docker inspect --format '{{.State.Status}}' "$1" 2>/dev/null || echo "not_found"
}

echo ""
echo -e "${BOLD}============================================================${RESET}"
echo -e "${BOLD}  Phase 1 / Step 1a — OE-Global-2 Verification${RESET}"
echo -e "${BOLD}============================================================${RESET}"
echo -e "  Run from: $(pwd)"
echo -e "  Date:     $(date)"

# ── Pre-flight: running from correct directory ────────────────────────────────
header "Pre-flight: working directory"
if [[ -f "docker-compose.yml" ]] && grep -q "openelisglobal-webapp" docker-compose.yml 2>/dev/null; then
  pass "Running from OpenELIS-Global-2 repo root"
else
  fail "Not in the OpenELIS-Global-2 repo root."
  echo -e "\n${RED}Run from: /Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/OpenELIS-Global-2${RESET}"
  exit 1
fi

# ── Check 1: All containers running ───────────────────────────────────────────
header "Check 1: All containers are running"
EXPECTED_CONTAINERS=("openelisglobal-database" "external-fhir-api" "openelisglobal-webapp"
                     "openelisglobal-front-end" "openelisglobal-proxy")

ALL_RUNNING=true
for c in "${EXPECTED_CONTAINERS[@]}"; do
  STATE=$(container_state "$c")
  if [[ "$STATE" == "running" ]]; then
    pass "$c is running"
  else
    fail "$c — state: ${STATE}"
    ALL_RUNNING=false
  fi
done

# oe-certs is an init container — should have exited 0
CERTS_EXIT=$(docker inspect --format '{{.State.ExitCode}}' oe-certs 2>/dev/null || echo "-1")
CERTS_STATE=$(container_state oe-certs)
if [[ "$CERTS_STATE" == "exited" && "$CERTS_EXIT" == "0" ]]; then
  pass "oe-certs exited 0 (expected — init container)"
else
  warn "oe-certs: state=${CERTS_STATE}, exit=${CERTS_EXIT} (expected exited/0)"
fi

if [[ "$ALL_RUNNING" == "false" ]]; then
  info "Tip: docker compose logs <container-name> to investigate"
fi

# ── Check 2: Database health ───────────────────────────────────────────────────
header "Check 2: Database is healthy"
# Note: docker compose exec uses SERVICE name (db.openelis.org), not container name
if wait_for "database" 6 10 docker compose exec -T db.openelis.org pg_isready -d clinlims -U clinlims; then
  pass "clinlims database is accepting connections"
else
  fail "clinlims database not ready after 60s"
  info "Tip: docker compose logs db.openelis.org"
fi

# ── Check 3: external-fhir-api metadata endpoint (via nginx proxy) ─────────────
header "Check 3: external-fhir-api FHIR metadata endpoint (https://localhost/fhir/)"
info "Waiting for HAPI FHIR to start (may take ~60s)..."
info "Access is via nginx proxy at https://localhost/fhir/ (nginx presents mTLS cert to FHIR store)"
if wait_for "external-fhir-api" 12 10 curl -sfk --max-time 5 https://localhost/fhir/metadata; then
  RESOURCE_TYPE=$(curl -sk https://localhost/fhir/metadata \
    | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('resourceType','MISSING'))" 2>/dev/null \
    || echo "PARSE_ERROR")
  if [[ "$RESOURCE_TYPE" == "CapabilityStatement" ]]; then
    pass "external-fhir-api returned CapabilityStatement (FHIR R4)"
    info "Endpoint: https://localhost/fhir/metadata"
  else
    fail "Unexpected resourceType: $RESOURCE_TYPE"
    info "Full response: $(curl -sk https://localhost/fhir/metadata | head -c 500)"
  fi
else
  fail "external-fhir-api not responding at https://localhost/fhir/metadata after 120s"
  info "Tip: docker compose logs external-fhir-api"
  info "Tip: docker compose logs openelisglobal-proxy"
fi

# ── Check 4: FHIR Patient POST ────────────────────────────────────────────────
header "Check 4: external-fhir-api accepts a FHIR Patient POST (via nginx proxy)"
POST_RESPONSE=$(curl -sk -X POST https://localhost/fhir/Patient \
  -H "Content-Type: application/fhir+json" \
  -d '{
    "resourceType": "Patient",
    "name": [{"family": "PoCTestPatient", "given": ["Step1a"]}],
    "birthDate": "1990-01-01",
    "gender": "male"
  }' 2>/dev/null || echo '{"resourceType":"ERROR"}')

POST_TYPE=$(echo "$POST_RESPONSE" | python3 -c \
  "import sys,json; d=json.load(sys.stdin); print(d.get('resourceType','MISSING'))" 2>/dev/null \
  || echo "PARSE_ERROR")
PATIENT_ID=$(echo "$POST_RESPONSE" | python3 -c \
  "import sys,json; d=json.load(sys.stdin); print(d.get('id',''))" 2>/dev/null \
  || echo "")

if [[ "$POST_TYPE" == "Patient" && -n "$PATIENT_ID" ]]; then
  pass "Patient created — ID: $PATIENT_ID"
else
  fail "Patient POST failed. resourceType=$POST_TYPE"
  info "Full response: $(echo "$POST_RESPONSE" | head -c 500)"
fi

# ── Check 5: FHIR Patient GET ─────────────────────────────────────────────────
header "Check 5: external-fhir-api returns the Patient on GET (via nginx proxy)"
if [[ -n "$PATIENT_ID" ]]; then
  GET_RESPONSE=$(curl -sk "https://localhost/fhir/Patient?family=PoCTestPatient" 2>/dev/null || echo '{}')
  TOTAL=$(echo "$GET_RESPONSE" | python3 -c \
    "import sys,json; d=json.load(sys.stdin); print(d.get('total',0))" 2>/dev/null \
    || echo "0")
  if [[ "$TOTAL" -ge 1 ]]; then
    pass "GET Patient returned total=$TOTAL"
  else
    fail "GET Patient returned total=0"
    info "GET response: $(echo "$GET_RESPONSE" | head -c 300)"
  fi
else
  warn "Skipping — no Patient ID from Check 4"
fi

# ── Check 7: OEG2 webapp API health ───────────────────────────────────────────
header "Check 7: OEG2 webapp API health"
info "Waiting for webapp to start (may take ~60s)..."
info "Health endpoint: https://localhost/api/OpenELIS-Global/health → {\"status\":\"UP\"}"
if wait_for "webapp" 12 10 curl -sk --max-time 5 https://localhost/api/OpenELIS-Global/health; then
  HEALTH=$(curl -sk https://localhost/api/OpenELIS-Global/health 2>/dev/null || echo '{}')
  STATUS=$(echo "$HEALTH" | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('status','MISSING'))" 2>/dev/null || echo "PARSE_ERROR")
  if [[ "$STATUS" == "UP" ]]; then
    pass "https://localhost/api/OpenELIS-Global/health → {\"status\":\"UP\"}"
  else
    fail "Health returned: $HEALTH (expected: {\"status\":\"UP\"})"
    info "Tip: docker compose logs oe.openelis.org | tail -50"
  fi
else
  fail "Webapp not responding at https://localhost/api/OpenELIS-Global/health after 120s"
  info "Tip: docker compose logs oe.openelis.org | tail -50"
fi

# ── Check 8: common.properties has FHIR polling URI ───────────────────────────
header "Check 8: common.properties has remote.source.uri configured"
# Note: docker compose exec uses SERVICE name (oe.openelis.org), not container name
REMOTE_URI=$(docker compose exec -T oe.openelis.org \
  grep "remote.source.uri" /run/secrets/common.properties 2>/dev/null || echo "")

if echo "$REMOTE_URI" | grep -q "fhir.openelis.org"; then
  pass "remote.source.uri is configured"
  info "$REMOTE_URI"
else
  fail "remote.source.uri is empty or missing in /run/secrets/common.properties"
  info "Fix: check volume/properties/common.properties and restart webapp"
fi

# ── Check 9: OEG2 logs show FHIR polling ──────────────────────────────────────
header "Check 9: OEG2 webapp shows FHIR polling activity in logs"
# Note: docker compose logs uses SERVICE name (oe.openelis.org), not container name
POLL_LOGS=$(docker compose logs oe.openelis.org 2>&1 \
  | grep -iE "fhir|task|poll|remote|FhirApiWork" \
  | tail -10 \
  || echo "")
if [[ -n "$POLL_LOGS" ]]; then
  pass "FHIR-related log lines found:"
  echo "$POLL_LOGS" | while IFS= read -r line; do info "  $line"; done
else
  warn "No FHIR polling log lines found yet — wait 20s and re-run (first poll cycle may not have fired)"
fi

# ── Check 10: Internal DNS — OEG2 can reach external-fhir-api via Docker network ──
header "Check 10: OEG2 webapp can reach external-fhir-api via nginx proxy (Docker-internal)"
info "external-fhir-api uses mTLS; nginx proxy (service name: proxy) presents the cert."
info "Testing: webapp → proxy/fhir/metadata (proxy handles mTLS to FHIR store)"
# Note: inside Docker, service DNS name is 'proxy' (not the container name 'openelisglobal-proxy')
INTERNAL_CHECK=$(docker compose exec -T oe.openelis.org \
  curl -sk --max-time 10 https://proxy/fhir/metadata 2>/dev/null \
  | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('resourceType','MISSING'))" 2>/dev/null \
  || echo "FAIL")

if [[ "$INTERNAL_CHECK" == "CapabilityStatement" ]]; then
  pass "openelisglobal-webapp → proxy → external-fhir-api ✓"
else
  fail "Cannot reach external-fhir-api from webapp container via proxy. Got: $INTERNAL_CHECK"
  info "Check Docker network: docker network inspect openelis-global-2_default"
  info "Tip: docker compose logs proxy | tail -20"
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

if [[ "$FAIL_COUNT" -eq 0 ]]; then
  echo -e "${GREEN}${BOLD}  ✓ Step 1a COMPLETE — all checks passed.${RESET}"
  echo -e "  Next: run Playwright UI tests, then proceed to Step 1b."
  echo ""
  echo -e "  Playwright UI tests:"
  echo -e "    cd /Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/OpenELIS-Global-2/frontend"
  echo -e "    TEST_USER=admin TEST_PASS='adminADMIN!' npx playwright test bahmni-poc-step1a --reporter=list"
  exit 0
else
  echo -e "${RED}${BOLD}  ✗ Step 1a INCOMPLETE — $FAIL_COUNT check(s) failed.${RESET}"
  echo -e "  Fix the failures above and re-run."
  exit 1
fi
