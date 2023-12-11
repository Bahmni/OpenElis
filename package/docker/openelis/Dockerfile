FROM amazoncorretto:8

ENV SERVER_PORT=8052
ENV BASE_DIR=/var/run/bahmni-lab
ENV CONTEXT_PATH=/openelis
ENV WAR_DIRECTORY=/var/run/bahmni-lab/bahmni-lab
ENV SERVER_OPTS="-Xms512m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m"
ENV DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,address=8001,server=y,suspend=n"
ENV OPENELIS_DB_SERVER='openelisdb'


RUN mkdir -p ${WAR_DIRECTORY}
RUN mkdir -p /opt/bahmni-lab/migrations/db_backup/
RUN mkdir -p /opt/bahmni-lab/migrations/liquibase/
RUN mkdir -p /opt/bahmni-lab/migrations/scripts/
RUN mkdir -p /home/bahmni/uploaded_results/
RUN mkdir -p /home/bahmni/uploaded-files/elis/

RUN yum install -y unzip
RUN yum install -y nc
RUN yum install -y postgresql

ADD https://raw.githubusercontent.com/eficode/wait-for/v2.2.3/wait-for wait-for.sh
ADD https://repo.mybahmni.org/packages/build/bahmni-embedded-tomcat-8.0.42.jar /opt/bahmni-lab/lib/bahmni-lab.jar
COPY openelis/dist/openelis.war /etc/bahmni-lab/openelis.war


RUN cd ${WAR_DIRECTORY} && jar xvf /etc/bahmni-lab/openelis.war

# Used by envsubst command for replacing environment values at runtime
RUN yum install -y gettext

COPY package/docker/openelis/templates/hibernate.cfg.xml.template /etc/bahmni-lab/
COPY package/docker/openelis/templates/atomfeed.properties.template /etc/bahmni-lab/
COPY package/resources/log4j2.xml ${WAR_DIRECTORY}/WEB-INF/classes/
COPY package/docker/openelis/update_openmrs_host_port.sh update_openmrs_host_port.sh
RUN chmod +x update_openmrs_host_port.sh
RUN chmod +x wait-for.sh

COPY OpenElis.zip /tmp/artifacts/
RUN unzip -d /tmp/artifacts/ /tmp/artifacts/OpenElis.zip
RUN cp -r /tmp/artifacts/OpenElis/db_backup/. /opt/bahmni-lab/migrations/db_backup/
RUN cp -r /tmp/artifacts/OpenElis/liquibase/. /opt/bahmni-lab/migrations/liquibase/
RUN cp -r /tmp/artifacts/OpenElis/scripts/. /opt/bahmni-lab/migrations/scripts/
COPY package/resources/migrateDb.sh /opt/bahmni-lab/migrations/scripts/
RUN chmod +x /opt/bahmni-lab/migrations/scripts/migrateDb.sh
COPY package/docker/openelis/run-implementation-openelis-implementation.sh /opt/bahmni-lab/migrations/scripts/
RUN chmod +x /opt/bahmni-lab/migrations/scripts/run-implementation-openelis-implementation.sh
RUN rm -rf /tmp/artifacts

COPY package/docker/openelis/start.sh start.sh
RUN chmod +x start.sh
CMD [ "./start.sh" ]
