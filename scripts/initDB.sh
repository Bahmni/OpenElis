#!/bin/sh

SCRIPTS_DIR=`dirname $0`
ROOT_DIR="$SCRIPTS_DIR/.."
DATABASE_NAME="clinlims"
set -e

if [ "$(psql -Upostgres -lqt | cut -d \| -f 1 | grep -w $DATABASE_NAME | wc -l)" -eq 0 ]; then
    echo "Creating database : $DATABASE_NAME"
    psql -U postgres -f $SCRIPTS_DIR/setupDB.sql
    pg_restore -U postgres -d $DATABASE_NAME $ROOT_DIR/db_backup/$1
else
    echo "The database $DATABASE_NAME already exits"
fi