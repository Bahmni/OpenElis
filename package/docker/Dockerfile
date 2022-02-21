FROM openjdk:8-alpine

ENV SERVER_PORT=8052
ENV BASE_DIR=/var/run/bahmni-lab
ENV CONTEXT_PATH=/openelis
ENV WAR_DIRECTORY=/var/run/bahmni-lab/bahmni-lab
ENV SERVER_OPTS="-Xms512m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m"
ENV DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,address=8001,server=y,suspend=n"
ENV OPENELIS_DB_SERVER='openelisdb'

COPY docker/bahmni-core.jar /opt/bahmni-lab/lib/bahmni-lab.jar
COPY resources/openelis.war /etc/bahmni-lab/openelis.war
COPY build/default_config/openelis /etc/bahmni_config/openelis/

RUN mkdir -p ${WAR_DIRECTORY}
RUN cd ${WAR_DIRECTORY} && jar xvf /etc/bahmni-lab/openelis.war

# Used by envsubst command for replacing environment values at runtime
RUN apk add gettext

COPY docker/templates/hibernate.cfg.xml.template /etc/bahmni-lab/
COPY docker/templates/atomfeed.properties.template /etc/bahmni-lab/
COPY resources/log4j2.xml ${WAR_DIRECTORY}/WEB-INF/classes/

COPY build/migrations/OpenElis/db_backup/ /opt/bahmni-lab/migrations/db_backup/
COPY build/migrations/OpenElis/liquibase/ /opt/bahmni-lab/migrations/liquibase/
COPY build/migrations/OpenElis/scripts/ /opt/bahmni-lab/migrations/scripts/
COPY resources/migrateDb.sh /opt/bahmni-lab/migrations/scripts/
RUN chmod +x /opt/bahmni-lab/migrations/scripts/migrateDb.sh
COPY docker/scripts/run-implementation-openelis-implementation.sh /opt/bahmni-lab/migrations/scripts/
RUN chmod +x /opt/bahmni-lab/migrations/scripts/run-implementation-openelis-implementation.sh

COPY docker/scripts/start.sh start.sh
RUN chmod +x start.sh
CMD [ "./start.sh" ]
