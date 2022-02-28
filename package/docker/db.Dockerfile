FROM postgres:9.6-alpine

COPY scripts/setupDB.sql /resources/setupDB.sql
COPY scripts/setupExtensions.sql /resources/setupExtensions.sql
COPY db_backup/openelis_schema.sql /resources/openelis_schema.sql
COPY db_backup/openelis_fresh_db_data.sql /resources/openelis_fresh_db_data.sql
COPY package/docker/scripts/configureDB.sh /docker-entrypoint-initdb.d/configureDB.sh
