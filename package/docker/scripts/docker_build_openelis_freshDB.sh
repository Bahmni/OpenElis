#!/bin/bash
set -xe

cd package 

#Extracting Migrations Zip
if [ ! -d build/migrations ]
then
mkdir -p build/migrations
fi

unzip -u -d build/migrations resources/OpenElis.zip

#Building Docker images
OPENELIS_IMAGE_TAG=${BAHMNI_VERSION}-${GITHUB_RUN_NUMBER}
docker build -t bahmni/openelis-db:fresh-${OPENELIS_IMAGE_TAG} -f docker/db.Dockerfile . --no-cache
