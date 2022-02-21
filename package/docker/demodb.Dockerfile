FROM postgres:9.6-alpine
COPY build/migrations/OpenElis/scripts/setupDB.sql /resources/setupDB.sql
COPY build/migrations/OpenElis/scripts/setupExtensions.sql /resources/setupExtensions.sql
COPY resources/openelis_demo_dump.sql /resources/openelis_demo_dump.sql
COPY docker/scripts/configureDemoDB.sh /docker-entrypoint-initdb.d/configureDemoDB.sh
