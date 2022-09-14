#!/usr/bin/env bash

revision='{
    "github_actions" : "https://github.com/Bahmni/OpenElis/actions/runs/GITHUB_RUN_ID/",
    "github": {
        "openelis" : "https://github.com/Bahmni/OpenElis/commit/_sha_"
    }
}'

replace() {
    envValue=`env | egrep "$2=" | sed "s/$2=//g"`
    sed "s/$1/$envValue/g"
}

echo $revision | replace "GITHUB_RUN_ID" "GITHUB_RUN_ID" | replace "_sha_" "GITHUB_SHA"