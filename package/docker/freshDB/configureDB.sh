#!/bin/bash

cd /resources/
psql -U postgres -f setupDB.sql
psql -U postgres -d clinlims -f setupExtensions.sql
if [ -f db-dump/*.dump ]; then
    echo "Loading datbase from dump file"
    for DB_DUMP_FILE in *.dump; do break; done
    pg_restore -U postgres -d clinlims db-dump/$DB_DUMP_FILE
else
    echo "Loading fresh database"
    psql -U clinlims -f openelis_schema.sql
    psql -U clinlims -f openelis_fresh_db_data.sql
fi
