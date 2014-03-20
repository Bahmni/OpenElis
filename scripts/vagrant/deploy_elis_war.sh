#!/bin/sh -x

TEMP_LOCATION=/tmp/deploy_openelis
ELIS_WAR_LOCATION=/home/jss/apache-tomcat-7.0.22/webapps

sudo rm -rf $ELIS_WAR_LOCATION/openelis
sudo cp -f $TEMP_LOCATION/* $ELIS_WAR_LOCATION
