#!/bin/bash

OPENELIS_DB_SERVER=localhost #default for manual/non ansible RPM installs

if [ -f /etc/bahmni-installer/bahmni.conf ]; then
. /etc/bahmni-installer/bahmni.conf
fi

#create bahmni user and group if doesn't exist
USERID=bahmni
GROUPID=bahmni
/bin/id -g $GROUPID 2>/dev/null
[ $? -eq 1 ]
groupadd bahmni

/bin/id $USERID 2>/dev/null
[ $? -eq 1 ]
useradd -g bahmni bahmni

mkdir -p /opt/bahmni-lab/uploaded-files
mkdir -p /opt/bahmni-lab/uploaded-files/elis

chown -R bahmni:bahmni /opt/bahmni-lab/uploaded-files/elis

#create links
ln -s /opt/bahmni-lab/etc /etc/bahmni-lab
ln -s /opt/bahmni-lab/log /var/log/bahmni-lab
ln -s /opt/bahmni-lab/uploaded-files/elis /home/bahmni/uploaded-files/elis

chown -R bahmni:bahmni /home/bahmni/uploaded-files/elis

setupConfFiles() {
    	rm -f /etc/httpd/conf.d/bahmnilab_ssl.conf
    	cp -f /opt/bahmni-lab/etc/bahmnilab_ssl.conf /etc/httpd/conf.d/bahmnilab_ssl.conf
}
setupConfFiles

#create a database if it doesn't exist and if it is not passive machine.
if [ "${IS_PASSIVE:-0}" -ne "1" ]; then
    RESULT_USER=`psql -U postgres -h$OPENELIS_DB_SERVER -tAc "SELECT count(*) FROM pg_roles WHERE rolname='clinlims'"`
    RESULT_DB=`psql -U postgres -h$OPENELIS_DB_SERVER -tAc "SELECT count(*) from pg_catalog.pg_database where datname='clinlims'"`
    if [ "$RESULT_USER" == "0" ]; then
        echo "creating postgres user - clinlims with roles CREATEDB,NOCREATEROLE,SUPERUSER,REPLICATION"
        createuser -Upostgres  -h$OPENELIS_DB_SERVER -d -R -s --replication clinlims;
    fi

    if [ "$RESULT_DB" == "0" ]; then
        if [ "${IMPLEMENTATION_NAME:-default}" = "default" ]; then
            createdb -Upostgres -h$OPENELIS_DB_SERVER clinlims;
            psql -Uclinlims -h$OPENELIS_DB_SERVER clinlims < /opt/bahmni-lab/db-dump/openelis_demo_dump.sql
        else
            (cd /opt/bahmni-lab/migrations && scripts/initDB.sh bahmni-base.dump)
        fi
    fi

    (cd /opt/bahmni-lab/migrations/liquibase/ && /opt/bahmni-lab/migrations/scripts/migrateDb.sh)
fi

cp -f /opt/bahmni-lab/etc/atomfeed.properties /opt/bahmni-lab/bahmni-lab/WEB-INF/classes/atomfeed.properties
cp -f /opt/bahmni-lab/etc/hibernate.cfg.xml /opt/bahmni-lab/bahmni-lab/WEB-INF/classes/us/mn/state/health/lims/hibernate/hibernate.cfg.xml

chkconfig --add bahmni-lab

# permissions
chown -R bahmni:bahmni /opt/bahmni-lab
chown -R bahmni:bahmni /var/log/bahmni-lab
chown -R bahmni:bahmni /etc/bahmni-lab

