OpenElis
========

forked from OpenElis_v3.1_r2013_02_21 branch in svn

Technical issues with the codebase
======================================

- Transaction and Hibernate session management
- Pagination handled via HttpSession
- Code duplication in various places (need examples here)

Functional changes made by us
=============================
- The order should contain the panel along with the tests. Hence making panel more than convenience tool for selecting multiple tests.
- AtomFeed based integration with OpenMRS and OpenERP
- REST endpoint for Patient, LabResults

Technical improvements
======================
- Added ant buld
- Shortcircuited all calls to Session.clear
