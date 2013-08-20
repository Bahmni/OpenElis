package org.bahmni.feed.openelis.feed.client;

import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.webclients.WebClient;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.quartz.Job;

public abstract class OpenMRSFeedReaderJob implements Job {
    private static final String FEED_NAME = "openmrs.patient.feed.uri";
    private static final String AUTH_URI = "openmrs.auth.uri";
    private static final String OPENMRS_USER = "openmrs.user";
    private static final String OPENMRS_PASSWORD = "openmrs.password";
    private static final String OPENMRS_WEBCLIENT_CONNECT_TIMEOUT = "openmrs.connectionTimeoutInMilliseconds";
    private static final String OPENMRS_WEBCLIENT_READ_TIMEOUT = "openmrs.replyTimeoutInMilliseconds";

    protected OpenMRSFeedReaderJob(Logger logger) {
        logger.info("Started");
    }

    protected AtomFeedClient createAtomFeedClient(AtomFeedProperties atomFeedProperties, AtomFeedClientFactory atomFeedClientFactory) {
        WebClient authenticatedWebClient = atomFeedClientFactory.getAuthenticatedOpenMRSWebClient(
                atomFeedProperties.getProperty(AUTH_URI),
                atomFeedProperties.getProperty(OPENMRS_USER),
                atomFeedProperties.getProperty(OPENMRS_PASSWORD),
                atomFeedProperties.getProperty(OPENMRS_WEBCLIENT_CONNECT_TIMEOUT),
                atomFeedProperties.getProperty(OPENMRS_WEBCLIENT_READ_TIMEOUT)
        );
        return atomFeedClientFactory.getMRSPatientFeedClient(atomFeedProperties,
                FEED_NAME, AUTH_URI, authenticatedWebClient);
    }
}