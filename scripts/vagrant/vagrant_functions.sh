#!/bin/bash 

#####################################################################################################
# This script can be used to call functions which will execute a command in your vagrant box. 
# -c option will be used to pass a command
# -f option will be used to pass a full qualified file that contains commands
#
# It can also be used to SCP into the vagrant box
#####################################################################################################

function run_in_vagrant {
    
    if [ "$1" == "-c" ]; then
		vagrant ssh -c "$2"
	elif [ "$1" == "-f" ]; then
		vagrant ssh -c < "$2"
	fi

}

# $1: Source  $2: Dest
function scp_to_vagrant {
    scp  -i $KEY_FILE $TIMEOUT $1 vagrant@$MACHINE_IP:$2
}