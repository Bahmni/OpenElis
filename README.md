OpenElis
========

[![Build and Publish OpenELIS](https://github.com/Bahmni/OpenElis/actions/workflows/build_publish_openelis.yml/badge.svg)](https://github.com/Bahmni/OpenElis/actions/workflows/build_publish_openelis.yml)

[![Build and Publish OpenELIS Demo DB](https://github.com/Bahmni/OpenElis/actions/workflows/build_publish_openelis_demoDB.yml/badge.svg)](https://github.com/Bahmni/OpenElis/actions/workflows/build_publish_openelis_demoDB.yml)

[![Build and Publish OpenELIS Fresh DB](https://github.com/Bahmni/OpenElis/actions/workflows/build_publish_openelis_freshDB.yml/badge.svg)](https://github.com/Bahmni/OpenElis/actions/workflows/build_publish_openelis_freshDB.yml)

forked from OpenElis_v3.1_r2013_02_21 branch in svn

*Requirements to build OpeneLIS*
* `java` version <= "1.7"
* `ant` version <= "1.9.1"
* `ruby` version == "3.1" and `gem install compass`

*To build OpenElis run*
* `ant dist`  Creates OpenELIS War
* `ant setupDB test test-only dist`  Creates clinlims database in postgres, runs tests, and then creates OpenELIS War
* `./scripts/vagrant-deploy.sh` Create OpenELIS War and deploys it to your vagrant's tomcat
* `./scripts/vagrant-database.sh` Runs Liquibase migration script in your vagrant 

## Bahmni OpenELIS (bahmni-lab) docker image
Docker images for [OpenELIS](https://hub.docker.com/r/bahmni/openelis/tags) and it's [database](https://hub.docker.com/r/bahmni/openelis-db/tags) are built using [Github Actions](/.github/workflows). 

Resources to build the following docker images can be found in the [package](/package) directory.
1. bahmni/openelis
2. bahmni/openelis-db:fresh
3. bahmni/openelis-db:demo

Transifex Configuration
===========================
Transifex is a web based translation platform where one can do the translations and can be pulled into the codebase.
[Link](http://docs.transifex.com/client/config/#transifexrc) to setup the Transifex Client 

* `tx pull -a` downloads the property files

For more information please refer this [link](https://bahmni.atlassian.net/wiki/display/BAH/Translating+Bahmni) 

Technical issues with the codebase
======================================

- Transaction and Hibernate session management
- Pagination handled via HttpSession
- Code duplication in various places (need examples here)
	- ResultValidationPaging, ResultsPaging and AnalyzerResultsPaging. Same copy-pasted code with very minor difference.

Functional changes made by us
=============================
- The order should contain the panel along with the tests. Hence making panel more than convenience tool for selecting multiple tests.
- AtomFeed based integration with OpenMRS and OpenERP
- REST endpoint for Patient, LabResults

- Added functionality to validate test results by a particular accession number. Also you can see items to be validated across all test sections.

Technical improvements
======================
- Added ant buld
- Shortcircuited all calls to Session.clear

