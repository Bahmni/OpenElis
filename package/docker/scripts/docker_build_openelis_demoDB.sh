#!/bin/bash
set -xe

#Fetching Database Backup Data
gunzip -f -k bahmni-scripts/demo/db-backups/v0.92/openelis_backup.sql.gz
cp bahmni-scripts/demo/db-backups/v0.92/openelis_backup.sql package/resources/openelis_demo_dump.sql
cd package

#Extracting Migrations Zip
if [ ! -d build/migrations ]
then
mkdir -p build/migrations
fi
unzip -u -d build/migrations resources/OpenElis.zip

#Building Docker image
OPENELIS_IMAGE_TAG=${BAHMNI_VERSION}-${GITHUB_RUN_NUMBER}
docker build -t bahmni/openelis-db:demo-${OPENELIS_IMAGE_TAG} -f docker/demodb.Dockerfile . --no-cache
