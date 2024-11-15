## OpenElis

[![Build and Publish OpenELIS](https://github.com/Bahmni/OpenElis/actions/workflows/build_publish_openelis.yml/badge.svg)](https://github.com/Bahmni/OpenElis/actions/workflows/build_publish_openelis.yml)

[![Build and Publish OpenELIS Demo DB](https://github.com/Bahmni/OpenElis/actions/workflows/build_publish_openelis_demoDB.yml/badge.svg)](https://github.com/Bahmni/OpenElis/actions/workflows/build_publish_openelis_demoDB.yml)

[![Build and Publish OpenELIS Fresh DB](https://github.com/Bahmni/OpenElis/actions/workflows/build_publish_openelis_freshDB.yml/badge.svg)](https://github.com/Bahmni/OpenElis/actions/workflows/build_publish_openelis_freshDB.yml)

Forked from the `OpenElis_v3.1_r2013_02_21` branch in SVN.

### Prerequisites

This project requires specific versions of tools to build and run OpenElis:

- **Java**: Version ≤ 1.7  
- **Ant**: Version ≤ 1.9.1  
- **Ruby**: Version = 3.1 (also requires `gem install compass`)

### Installation Steps

#### Option 1: Manual Installation

Run the following commands to build OpenElis:

1. To create the OpenELIS WAR file:
   ```bash
   ant dist
   ```
2. To set up the database, run tests, and create the WAR file:
   ```bash
   ant setupDB test test-only dist
   ```
3. To create and deploy to Vagrant's Tomcat:
   ```bash
   ./scripts/vagrant-deploy.sh
   ```
4. To run Liquibase migration script in Vagrant:
   ```bash
   ./scripts/vagrant-database.sh
   ```

### Docker Images

Docker images for OpenElis and its database are built using GitHub Actions.

- [bahmni/openelis](https://hub.docker.com/r/bahmni/openelis/tags)
- [bahmni/openelis-db:fresh](https://hub.docker.com/r/bahmni/openelis-db/tags)
- [bahmni/openelis-db:demo](https://hub.docker.com/r/bahmni/openelis-db/tags)

Resources for building these images can be found in the [package directory](/package).

### Transifex Configuration

Transifex is a web-based translation platform for managing translations. To set up the Transifex Client, follow the [documentation](http://docs.transifex.com/client/config/#transifexrc).

- To pull property files:
  ```bash
  tx pull -a
  ```
Refer to the [Bahmni Translating Guide](https://bahmni.atlassian.net/wiki/display/BAH/Translating+Bahmni) for more details.

### Functional Changes

The following functional improvements have been made:

- Orders now contain panels along with tests, making the panel more than a convenience tool for selecting multiple tests.
- Integration with OpenMRS and OpenERP using AtomFeed.
- Added REST endpoints for Patient and LabResults.
- Introduced functionality to validate test results by accession number, along with a view for items needing validation across sections.

### Technical Improvements

- Added Ant build support.
- Optimized calls to `Session.clear`.

### Known Issues

- Transaction and Hibernate session management challenges.
- Pagination handled via `HttpSession`.
- Code duplication in the following areas:
  - ResultValidationPaging, ResultsPaging, and AnalyzerResultsPaging (same copy-pasted code with minor differences).

