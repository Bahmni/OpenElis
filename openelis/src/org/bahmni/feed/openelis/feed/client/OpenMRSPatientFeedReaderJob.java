package org.bahmni.feed.openelis.feed.client;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.event.PatientFeedWorker;
import org.bahmni.feed.openelis.webclient.WebClient;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class OpenMRSPatientFeedReaderJob implements Job {
    private AtomFeedProperties atomFeedProperties;
    private static final String FEED_NAME = "openmrs.patient.feed.uri";
    private static final String AUTH_URI = "openmrs.auth.uri";
    private static final String OPENMRS_USER = "openmrs.user";
    private static final String OPENMRS_PASSWORD = "openmrs.password";
    private AtomFeedClient atomFeedClient;
    private static Logger logger = Logger.getLogger(OpenMRSPatientFeedReaderJob.class);

    public OpenMRSPatientFeedReaderJob() {
        this(AtomFeedProperties.getInstance(), AtomFeedClientFactory.getFeedClient());
    }

    public OpenMRSPatientFeedReaderJob(AtomFeedProperties atomFeedProperties, AtomFeedClient atomFeedClient) {
        this.atomFeedProperties = atomFeedProperties;
        this.atomFeedClient = atomFeedClient;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            WebClient webClient = new WebClient();
            URI uri = URI.create(atomFeedProperties.getProperty(AUTH_URI));
            HashMap<String, String> headers = new HashMap<String, String>();
            String authorizationHeaderValue = String.format("Basic %s:%s", atomFeedProperties.getProperty(OPENMRS_USER), atomFeedProperties.getProperty(OPENMRS_PASSWORD));
            headers.put("Authorization", new String(Base64.encodeBase64(authorizationHeaderValue.getBytes())));
            headers.put("Disable-WWW-Authenticate", "true");
            webClient.get(uri, headers);

            URL openMRSAuthURL = new URL(atomFeedProperties.getProperty(AUTH_URI));
            String urlPrefix = String.format("%s://%s", openMRSAuthURL.getProtocol(), openMRSAuthURL.getAuthority());
            atomFeedClient.processEvents(new URI(atomFeedProperties.getProperty(FEED_NAME)), new PatientFeedWorker(webClient, urlPrefix));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new JobExecutionException(e);
        }
    }
}