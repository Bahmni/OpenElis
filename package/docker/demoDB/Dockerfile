FROM postgres:9.6-alpine

COPY scripts/database/setupDB.sql /resources/setupDB.sql
COPY scripts/database/setupExtensions.sql /resources/setupExtensions.sql
COPY package/resources/openelis_demo_dump.sql /resources/openelis_demo_dump.sql
COPY package/docker/demoDB/configureDemoDB.sh /docker-entrypoint-initdb.d/configureDemoDB.sh
