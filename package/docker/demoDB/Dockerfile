FROM postgres:9.6-alpine

ADD https://github.com/Bahmni/bahmni-scripts/blob/master/demo/db-backups/v0.92/openelis_backup.sql.gz\?raw\=true /resources/
RUN gunzip /resources/openelis_backup.sql.gz && chmod +rx /resources/*.sql && rm -f /resources/openelis_backup.sql.gz
COPY scripts/database/setupDB.sql /resources/setupDB.sql
COPY scripts/database/setupExtensions.sql /resources/setupExtensions.sql
COPY package/docker/demoDB/configureDemoDB.sh /docker-entrypoint-initdb.d/configureDemoDB.sh
