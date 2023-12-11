#!/bin/sh
set -e

replaceConfigFiles(){
    ATOMFEED_PROPERTIES_FILE=${WAR_DIRECTORY}/WEB-INF/classes/atomfeed.properties
    HIBERNATE_CONFIG_FILE=${WAR_DIRECTORY}/WEB-INF/classes/us/mn/state/health/lims/hibernate/hibernate.cfg.xml
    envsubst < /etc/bahmni-lab/atomfeed.properties.template > ${ATOMFEED_PROPERTIES_FILE}
    envsubst < /etc/bahmni-lab/hibernate.cfg.xml.template > ${HIBERNATE_CONFIG_FILE}
}

echo "Waiting for ${OPENELIS_DB_SERVER}:5432 for 3600 seconds"
sh wait-for.sh --timeout=3600 ${OPENELIS_DB_SERVER}:5432
# Linking bahmni config
rm -rf /var/www/bahmni_config/
mkdir -p /var/www/bahmni_config/
ln -s /etc/bahmni_config/openelis /var/www/bahmni_config/openelis

replaceConfigFiles
./update_openmrs_host_port.sh
echo "[INFO] Running Default Liquibase migrations"
cd /opt/bahmni-lab/migrations/liquibase/ && sh /opt/bahmni-lab/migrations/scripts/migrateDb.sh
echo "[INFO] Running User Defined Liquibase migrations"
sh /opt/bahmni-lab/migrations/scripts/run-implementation-openelis-implementation.sh
echo "[INFO] Starting Application"
java -jar $SERVER_OPTS $DEBUG_OPTS /opt/bahmni-lab/lib/bahmni-lab.jar
