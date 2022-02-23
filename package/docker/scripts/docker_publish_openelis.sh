#!/bin/bash
set -e
OPENELIS_IMAGE_TAG=${BAHMNI_VERSION}-${GITHUB_RUN_NUMBER}
echo ${DOCKER_HUB_TOKEN} | docker login -u ${DOCKER_HUB_USERNAME} --password-stdin
echo "[INFO] Pushing build image for openelis"
docker push bahmni/openelis:${OPENELIS_IMAGE_TAG}

echo "[INFO] Tagging build image for openelis as SNAPSHOT Images"
OPENELIS_SNAPSHOT_IMAGE_TAG=${BAHMNI_VERSION}-SNAPSHOT
docker tag bahmni/openelis:${OPENELIS_IMAGE_TAG} bahmni/openelis:${OPENELIS_SNAPSHOT_IMAGE_TAG}

echo "[INFO] Pushing SNAPSHOT image for openelis"
docker push bahmni/openelis:${OPENELIS_SNAPSHOT_IMAGE_TAG}

docker logout
