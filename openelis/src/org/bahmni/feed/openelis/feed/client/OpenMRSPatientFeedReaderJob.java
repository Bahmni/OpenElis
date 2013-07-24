package org.bahmni.feed.openelis.feed.client;

import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.webclient.WebClient;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class OpenMRSPatientFeedReaderJob implements Job {
    private AtomFeedProperties atomFeedProperties;
    private AtomFeedClient atomFeedClient;

    private static final String FEED_NAME = "openmrs.patient.feed.uri";
    private static final String AUTH_URI = "openmrs.auth.uri";
    private static final String OPENMRS_USER = "openmrs.user";
    private static final String OPENMRS_PASSWORD = "openmrs.password";
    private static final String OPENMRS_WEBCLIENT_CONNECT_TIMEOUT = "openmrs.connectionTimeoutInMilliseconds";
    private static final String OPENMRS_WEBCLIENT_READ_TIMEOUT = "openmrs.replyTimeoutInMilliseconds";
    private static Logger logger = Logger.getLogger(OpenMRSPatientFeedReaderJob.class);
    private WebClient authenticatedWebClient;

    public OpenMRSPatientFeedReaderJob() {
        this(AtomFeedProperties.getInstance(), new AtomFeedClientFactory());
    }

    OpenMRSPatientFeedReaderJob(AtomFeedProperties atomFeedProperties, AtomFeedClientFactory atomFeedClientFactory) {
        this.atomFeedProperties = atomFeedProperties;
        this.authenticatedWebClient = atomFeedClientFactory.getAuthenticatedWebClient(
                atomFeedProperties.getProperty(AUTH_URI),
                atomFeedProperties.getProperty(OPENMRS_USER),
                atomFeedProperties.getProperty(OPENMRS_PASSWORD),
                atomFeedProperties.getProperty(OPENMRS_WEBCLIENT_CONNECT_TIMEOUT),
                atomFeedProperties.getProperty(OPENMRS_WEBCLIENT_READ_TIMEOUT)
                );
        this.atomFeedClient = atomFeedClientFactory.getMRSPatientFeedClient(atomFeedProperties,
                FEED_NAME, AUTH_URI, authenticatedWebClient);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            atomFeedClient.processEvents();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}