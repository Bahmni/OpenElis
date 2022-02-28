#!/bin/bash
set -xe

#Building Docker image
OPENELIS_IMAGE_TAG=${BAHMNI_VERSION}-${GITHUB_RUN_NUMBER}
docker build -t bahmni/openelis-db:fresh-${OPENELIS_IMAGE_TAG} -f package/docker/db.Dockerfile . --no-cache
