package org.bahmni.feed.openelis.feed.job.openmrs;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.event.LabFeedEventWorker;
import org.bahmni.feed.openelis.feed.job.FeedNames;
import org.bahmni.feed.openelis.feed.job.OpenELISFeedFailedEventsJob;
import org.bahmni.webclients.Authenticator;
import org.bahmni.webclients.ConnectionDetails;
import org.bahmni.webclients.HttpClient;
import org.bahmni.webclients.openmrs.OpenMRSLoginAuthenticator;
import org.ict4h.atomfeed.client.service.EventWorker;

public class OpenMRSLabFeedFailedEventsJob extends OpenELISFeedFailedEventsJob {
    private static final String AUTH_URI = "openmrs.auth.uri";
    private static final String OPENMRS_USER = "openmrs.user";
    private static final String OPENMRS_PASSWORD = "openmrs.password";
    private static final String OPENMRS_WEBCLIENT_CONNECT_TIMEOUT = "openmrs.connectionTimeoutInMilliseconds";
    private static final String OPENMRS_WEBCLIENT_READ_TIMEOUT = "openmrs.replyTimeoutInMilliseconds";

    protected ConnectionDetails getConnectionDetails() {
        AtomFeedProperties atomFeedProperties = AtomFeedProperties.getInstance();
        return new ConnectionDetails(
                atomFeedProperties.getProperty(AUTH_URI),
                atomFeedProperties.getProperty(OPENMRS_USER),
                atomFeedProperties.getProperty(OPENMRS_PASSWORD),
                Integer.parseInt(atomFeedProperties.getProperty(OPENMRS_WEBCLIENT_CONNECT_TIMEOUT)),
                Integer.parseInt(atomFeedProperties.getProperty(OPENMRS_WEBCLIENT_READ_TIMEOUT)));
    }

    @Override
    protected EventWorker createWorker(HttpClient authenticatedWebClient, String urlPrefix) {
        return new LabFeedEventWorker(authenticatedWebClient, urlPrefix);
    }

    @Override
    protected String getFeedName() {
        return FeedNames.OPENMRS_LAB_FEED_NAME;
    }

    @Override
    protected Authenticator getAuthenticator(ConnectionDetails connectionDetails) {
        return new OpenMRSLoginAuthenticator(connectionDetails);
    }
}
