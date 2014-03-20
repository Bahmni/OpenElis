#!/bin/sh
MACHINE_IP=192.168.33.10
KEY_FILE=~/.vagrant.d/insecure_private_key
ssh vagrant@$MACHINE_IP -i $KEY_FILE "sudo su - jss && cd /bahmni_temp/OpenElis/ && ant setupDB"