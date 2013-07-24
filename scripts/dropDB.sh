SCRIPTS_DIR=`dirname $0`
psql -U postgres -f $SCRIPTS_DIR/terminate-connections.sql
dropdb -U postgres clinlims