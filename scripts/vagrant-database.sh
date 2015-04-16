#!/bin/sh -x

PATH_OF_CURRENT_SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source $PATH_OF_CURRENT_SCRIPT/vagrant/vagrant_functions.sh

run_in_vagrant -c "sudo su -c 'cd /bahmni/OpenElis/ && /home/bahmni/apache-ant-1.9.1/bin/ant setupDB'"