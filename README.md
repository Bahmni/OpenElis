OpenElis
========

[![Build Status](https://travis-ci.org/Bhamni/OpenElis.svg?branch=master)](https://travis-ci.org/Bhamni/OpenElis)

forked from OpenElis_v3.1_r2013_02_21 branch in svn

*To build OpenElis run*
* `ant dist`  Creates OpenELIS War
* `ant setupDB test test-only dist`  Creates clinlims database in postgres, runs tests, and then creates OpenELIS War
* `./scripts/vagrant-deploy.sh` Create OpenELIS War and deploys it to your vagrant's tomcat
* `./scripts/vagrant-database.sh` Runs Liquibase migration script in your vagrant 

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

