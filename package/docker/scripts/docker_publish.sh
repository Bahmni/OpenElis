#!/bin/bash
set -e
OPENELIS_IMAGE_TAG=${BAHMNI_VERSION}-${GITHUB_RUN_NUMBER}
echo ${DOCKER_HUB_TOKEN} | docker login -u ${DOCKER_HUB_USERNAME} --password-stdin
echo "[INFO] Pushing build images"
docker push bahmni/openelis-db:fresh-${OPENELIS_IMAGE_TAG}
docker push bahmni/openelis-db:demo-${OPENELIS_IMAGE_TAG}
docker push bahmni/openelis:${OPENELIS_IMAGE_TAG}

echo "[INFO] Tagging build images as SNAPSHOT Images"
OPENELIS_SNAPSHOT_IMAGE_TAG=${BAHMNI_VERSION}-SNAPSHOT
docker tag bahmni/openelis-db:fresh-${OPENELIS_IMAGE_TAG} bahmni/openelis-db:fresh-${OPENELIS_SNAPSHOT_IMAGE_TAG}
docker tag bahmni/openelis-db:demo-${OPENELIS_IMAGE_TAG} bahmni/openelis-db:demo-${OPENELIS_SNAPSHOT_IMAGE_TAG}
docker tag bahmni/openelis:${OPENELIS_IMAGE_TAG} bahmni/openelis:${OPENELIS_SNAPSHOT_IMAGE_TAG}

echo "[INFO] Pushing SNAPSHOT images"
docker push bahmni/openelis-db:fresh-${OPENELIS_SNAPSHOT_IMAGE_TAG}
docker push bahmni/openelis-db:demo-${OPENELIS_SNAPSHOT_IMAGE_TAG}
docker push bahmni/openelis:${OPENELIS_SNAPSHOT_IMAGE_TAG}

docker logout
