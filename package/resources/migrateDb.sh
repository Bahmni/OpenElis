#!/bin/sh
set -e -x

if [ -f /etc/bahmni-lab/bahmni-lab.conf ]; then
. /etc/bahmni-lab/bahmni-lab.conf
fi
if [ -f /etc/bahmni-installer/bahmni.conf ]; then
. /etc/bahmni-installer/bahmni.conf
fi

java -jar -Dfile.encoding=utf-8 lib/liquibase-1.9.5.jar --defaultsFile=liquibase.properties --contexts=bahmni --defaultSchemaName=clinlims --url=jdbc:postgresql://$OPENELIS_DB_SERVER:$OPENELIS_DB_PORT/$OPENELIS_DB_NAME --username=$OPENELIS_DB_USERNAME --password=$OPENELIS_DB_PASSWORD update
