#!/bin/bash
set -xe

#Copying bahmni core which has embedded Tomcat Server
cp core-1.0-SNAPSHOT.jar package/docker/bahmni-core.jar

# Packaging default config to embed into default image
cp default_config.zip package/resources/

cd package

#Extracting Migrations Zip
if [ ! -d build/migrations ]
then
mkdir -p build/migrations
fi
unzip -u -d build/migrations resources/OpenElis.zip

# Unzipping Default Config
unzip -q -u -d build/default_config resources/default_config.zip

#Building Docker images
OPENELIS_IMAGE_TAG=${BAHMNI_VERSION}-${GITHUB_RUN_NUMBER}
docker build -t bahmni/openelis:${OPENELIS_IMAGE_TAG} -t bahmni/openelis:latest -f docker/openelis/Dockerfile . --no-cache
