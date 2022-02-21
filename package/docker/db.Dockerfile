FROM postgres:9.6-alpine

COPY build/migrations/OpenElis/scripts/setupDB.sql /resources/setupDB.sql
COPY build/migrations/OpenElis/scripts/setupExtensions.sql /resources/setupExtensions.sql
COPY build/migrations/OpenElis/db_backup/openelis_schema.sql /resources/openelis_schema.sql
COPY build/migrations/OpenElis/db_backup/openelis_fresh_db_data.sql /resources/openelis_fresh_db_data.sql
COPY docker/scripts/configureDB.sh /docker-entrypoint-initdb.d/configureDB.sh
