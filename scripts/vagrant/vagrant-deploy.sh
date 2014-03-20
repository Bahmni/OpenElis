#!/bin/bash -x

#All config is here
TEMP_ELIS_WAR_FOLDER=/tmp/deploy_openelis
MACHINE_IP=192.168.33.10
CWD=$1
SCRIPTS_DIR=$CWD/scripts/vagrant
KEY_FILE=~/.vagrant.d/insecure_private_key
TIMEOUT="-o ConnectTimeout=5"

# Setup environment
ssh vagrant@$MACHINE_IP -i $KEY_FILE $TIMEOUT < $SCRIPTS_DIR/setup_environment.sh

# Copy ELIS War file to Vagrant tmp
scp  -i $KEY_FILE $TIMEOUT $2 vagrant@$MACHINE_IP:$TEMP_ELIS_WAR_FOLDER

#Deploy them from Vagrant /tmp to appropriate location
ssh vagrant@$MACHINE_IP -i $KEY_FILE $TIMEOUT < $SCRIPTS_DIR/deploy_elis_war.sh
