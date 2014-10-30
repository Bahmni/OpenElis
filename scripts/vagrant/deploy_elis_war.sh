#!/bin/sh -x

TEMP_LOCATION=/tmp/deploy_openelis
ELIS_WAR_LOCATION=/home/jss/apache-tomcat-8.0.12/webapps

sudo rm -rf $ELIS_WAR_LOCATION/openelis
sudo su - jss -c  "cp -f $TEMP_LOCATION/* $ELIS_WAR_LOCATION"
