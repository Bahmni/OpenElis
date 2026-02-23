#!/usr/bin/env bash
# =============================================================================
# Phase 1, Step 1a — Teardown
# =============================================================================
# Stops and removes all OEG2 containers after the demo.
# Data volumes are preserved (so the next demo starts faster).
# Pass --volumes to also remove data volumes (full clean slate).
#
# Usage:
#   bash /Users/vishalkarmalkar/IdeaProjects/bahmni/OpenElis/docs/steps/scripts/phase1-step1a-teardown.sh
#   bash /Users/vishalkarmalkar/IdeaProjects/bahmni/OpenElis/docs/steps/scripts/phase1-step1a-teardown.sh --volumes
# =============================================================================

OEG2_DIR="/Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/OpenELIS-Global-2"

BOLD='\033[1m'
GREEN='\033[0;32m'
CYAN='\033[0;36m'
RESET='\033[0m'

cd "$OEG2_DIR"

echo ""
echo -e "${BOLD}Phase 1 / Step 1a — Teardown${RESET}"

if [[ "${1:-}" == "--volumes" ]]; then
  echo -e "  Removing containers, networks, ${CYAN}and data volumes${RESET}..."
  docker compose down --remove-orphans --volumes 2>&1 | grep -v "^$" | sed 's/^/  /'
  echo -e "  ${GREEN}✓ Full teardown complete (volumes removed)${RESET}"
else
  echo -e "  Removing containers and networks (keeping data volumes)..."
  docker compose down --remove-orphans 2>&1 | grep -v "^$" | sed 's/^/  /'
  echo -e "  ${GREEN}✓ Teardown complete (data volumes preserved)${RESET}"
  echo -e "  To also remove volumes: bash $0 --volumes"
fi
echo ""
