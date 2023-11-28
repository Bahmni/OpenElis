#!/bin/bash

cd /resources/
psql -U postgres -f setupDB.sql
psql -U postgres -d clinlims -f setupExtensions.sql
psql -U clinlims -d clinlims -f openelis_backup.sql
