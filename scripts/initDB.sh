#!/bin/sh

SCRIPTS_DIR=`dirname $0`
ROOT_DIR="$SCRIPTS_DIR/.."
DATABASE_NAME="clinlims"
OPENELIS_DB_SERVER="localhost"

if [ -f /etc/bahmni-installer/bahmni.conf ]; then
. /etc/bahmni-installer/bahmni.conf
fi
# set -e

if [ "$(psql -Upostgres -h $OPENELIS_DB_SERVER -lqt | cut -d \| -f 1 | grep -w $DATABASE_NAME | wc -l)" -eq 0 ]; then
    echo "Creating database : $DATABASE_NAME"
    psql -U postgres -h $OPENELIS_DB_SERVER -f $SCRIPTS_DIR/setupDB.sql
    pg_restore -U postgres -h $OPENELIS_DB_SERVER -d $DATABASE_NAME $ROOT_DIR/db_backup/$1
else
    echo "The database $DATABASE_NAME already exits"
fi

echo "Adding required extensions"
psql -U postgres -h $OPENELIS_DB_SERVER -d clinlims -f $SCRIPTS_DIR/setupExtensions.sql