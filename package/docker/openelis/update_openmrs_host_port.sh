#!/bin/bash
set +e

run_sql() {
  #TODO: Remove hardcoded password and use env variable --> BAH-3394
  PGPASSWORD="clinlims" psql --host="${OPENELIS_DB_SERVER}" -U clinlims -d clinlims -t -c "$1"
}

if [ $(run_sql "select count(*) from information_schema.tables where table_name='markers' and table_schema='clinlims';") -gt 0 ]
then
    echo "Updating OpenMRS Host Port in markers and failed_events table"
    run_sql "UPDATE markers SET feed_uri_for_last_read_entry = regexp_replace(feed_uri_for_last_read_entry, 'http://.*/openmrs', 'http://${OPENMRS_HOST}:${OPENMRS_PORT}/openmrs'),feed_uri = regexp_replace(feed_uri, 'http://.*/openmrs', 'http://${OPENMRS_HOST}:${OPENMRS_PORT}/openmrs') where feed_uri ~ 'openmrs';"
    run_sql "UPDATE failed_events SET feed_uri = regexp_replace(feed_uri, 'http://.*/openmrs', 'http://${OPENMRS_HOST}:${OPENMRS_PORT}/openmrs') where feed_uri ~'openmrs';"
fi
