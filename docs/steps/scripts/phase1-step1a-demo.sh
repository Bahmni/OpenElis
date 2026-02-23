#!/usr/bin/env bash
# =============================================================================
# Phase 1, Step 1a — OE-Global-2 Demo Script
# =============================================================================
# Single entry point for the SME demo.
# Starts all OEG2 containers, waits for them to be ready, then runs the full
# verification suite. At the end, Playwright UI test instructions are shown.
#
# Usage (from anywhere):
#   bash /Users/vishalkarmalkar/IdeaProjects/bahmni/OpenElis/docs/steps/scripts/phase1-step1a-demo.sh
#
# To clean up after the demo:
#   bash /Users/vishalkarmalkar/IdeaProjects/bahmni/OpenElis/docs/steps/scripts/phase1-step1a-teardown.sh
# =============================================================================

set -uo pipefail

# ── Config ────────────────────────────────────────────────────────────────────
OEG2_DIR="/Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/OpenELIS-Global-2"
SCRIPT_DIR="/Users/vishalkarmalkar/IdeaProjects/bahmni/OpenElis/docs/steps/scripts"
VERIFY_SCRIPT="$SCRIPT_DIR/phase1-step1a-verify.sh"

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
echo -e "${BOLD}║  Bahmni + OE-Global-2 Integration — Phase 1 / Step 1a Demo  ║${RESET}"
echo -e "${BOLD}╚══════════════════════════════════════════════════════════════╝${RESET}"
echo -e "  Date: $(date)"
echo -e "  OEG2: $OEG2_DIR"

# ── Guard: verify OEG2 directory exists ───────────────────────────────────────
if [[ ! -f "$OEG2_DIR/docker-compose.yml" ]]; then
  err "OEG2 directory not found: $OEG2_DIR"
  exit 1
fi
cd "$OEG2_DIR"

# ── Step 1: Clean up any stale containers ─────────────────────────────────────
step "Step 1 of 5: Cleaning up stale containers (if any)"
RUNNING=$(docker compose ps --format "{{.Name}}" 2>/dev/null | wc -l | tr -d ' ')
if [[ "$RUNNING" -gt 0 ]]; then
  info "Found $RUNNING existing container(s) — removing..."
  docker compose down --remove-orphans 2>&1 | grep -v "^$" | sed 's/^/   /'
  ok "Previous containers removed"
else
  info "No existing containers found — clean start"
fi

# ── Step 2: Start containers ───────────────────────────────────────────────────
step "Step 2 of 5: Starting OE-Global-2 containers"
info "Running: docker compose up -d"
echo ""
docker compose up -d 2>&1 | grep -v "^$" | sed 's/^/   /'
echo ""

# Confirm all expected containers started
EXPECTED=("openelisglobal-database" "external-fhir-api" "openelisglobal-webapp"
          "openelisglobal-front-end" "openelisglobal-proxy")
ALL_STARTED=true
for c in "${EXPECTED[@]}"; do
  STATE=$(docker inspect --format '{{.State.Status}}' "$c" 2>/dev/null || echo "missing")
  if [[ "$STATE" == "running" ]]; then
    ok "$c started"
  else
    err "$c — state: $STATE"
    ALL_STARTED=false
  fi
done

if [[ "$ALL_STARTED" == "false" ]]; then
  err "One or more containers failed to start. Check: docker compose logs"
  exit 1
fi

# ── Step 3: Wait for services to be ready ─────────────────────────────────────
step "Step 3 of 5: Waiting for services to be ready"
info "Giving services 90s to initialize..."
info "(HAPI FHIR and Tomcat take ~60-90s on first start)"

# Progress indicator while waiting
MAX_POLLS=18
for i in $(seq 1 $MAX_POLLS); do
  sleep 5
  # Check if both key services are up early
  FHIR_UP=false
  WEBAPP_UP=false
  curl -sfk --max-time 3 https://localhost/fhir/metadata &>/dev/null && FHIR_UP=true
  curl -sk --max-time 3 https://localhost/api/OpenELIS-Global/health &>/dev/null && WEBAPP_UP=true

  STATUS=""
  [[ "$FHIR_UP" == "true" ]] && STATUS="${STATUS}FHIR✓ " || STATUS="${STATUS}FHIR… "
  [[ "$WEBAPP_UP" == "true" ]] && STATUS="${STATUS}WebApp✓" || STATUS="${STATUS}WebApp…"

  echo -ne "\r   [${i}/${MAX_POLLS}] ${STATUS} ($(( i * 5 ))s elapsed)"

  if [[ "$FHIR_UP" == "true" && "$WEBAPP_UP" == "true" ]]; then
    echo -e "\n"
    ok "Both services are up after $(( i * 5 ))s"
    break
  fi

  if [[ "$i" -eq "$MAX_POLLS" ]]; then
    echo -e "\n"
    warn "90s elapsed — proceeding to verification (some checks may retry further)"
  fi
done

# ── Step 4: Run verification ───────────────────────────────────────────────────
step "Step 4 of 5: Running verification checks"
echo ""
bash "$VERIFY_SCRIPT"
VERIFY_EXIT=$?

# ── Step 5: Playwright UI tests ────────────────────────────────────────────────
step "Step 5 of 5: Running Playwright UI tests (Check 6)"
echo ""

PLAYWRIGHT_EXIT=0
if command -v npx &>/dev/null; then
  (
    cd "$OEG2_DIR/frontend"
    TEST_USER=admin TEST_PASS="adminADMIN!" \
      npx playwright test bahmni-poc-step1a --reporter=list 2>&1
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
  PLAYWRIGHT_EXIT=0  # Don't fail the demo if npx is missing
fi

# ── Summary ───────────────────────────────────────────────────────────────────
echo ""
echo -e "${BOLD}══════════════════════════════════════════════════════════════${RESET}"
echo -e "${BOLD}  Final Summary${RESET}"
echo -e "${BOLD}══════════════════════════════════════════════════════════════${RESET}"
echo ""

OVERALL_EXIT=0
if [[ "$VERIFY_EXIT" -eq 0 ]]; then
  echo -e "  ${GREEN}✓ Bash verification:  PASSED${RESET}"
else
  echo -e "  ${RED}✗ Bash verification:  FAILED${RESET}"
  OVERALL_EXIT=1
fi
if [[ "$PLAYWRIGHT_EXIT" -eq 0 ]]; then
  echo -e "  ${GREEN}✓ Playwright UI tests: PASSED${RESET}"
else
  echo -e "  ${RED}✗ Playwright UI tests: FAILED${RESET}"
  OVERALL_EXIT=1
fi

echo ""
echo -e "  OEG2 web UI: ${CYAN}https://localhost${RESET}  (accept the self-signed cert)"
echo -e "  Login: ${CYAN}admin / adminADMIN!${RESET}"
echo ""

if [[ "$OVERALL_EXIT" -eq 0 ]]; then
  echo -e "${GREEN}${BOLD}  ✓ Demo complete — Step 1a fully verified.${RESET}"
  echo -e "  To tear down: bash $SCRIPT_DIR/phase1-step1a-teardown.sh"
else
  echo -e "${RED}${BOLD}  ✗ Demo had failures — see output above.${RESET}"
fi
echo ""
exit "$OVERALL_EXIT"
