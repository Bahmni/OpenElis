#!/bin/sh -x

TEMP_LOCATION=/tmp/deploy_openelis

if [[ ! -d $TEMP_LOCATION ]]; then
   mkdir $TEMP_LOCATION
fi

rm -rf $TEMP_LOCATION/*