package org.bahmni.feed.openelis.feed.client;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.bahmni.feed.openelis.feed.FeedException;
import org.bahmni.feed.openelis.feed.event.PatientFeedWorker;
import org.bahmni.feed.openelis.utils.OpenElisConnectionProvider;
import org.bahmni.webclients.WebClient;
import org.bahmni.webclients.openmrs.OpenMRSAuthenticationResponse;
import org.bahmni.webclients.openmrs.OpenMRSAuthenticator;
import org.ict4h.atomfeed.client.factory.AtomFeedClientBuilder;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class AtomFeedClientFactory {
    static AtomFeedClient getERPLabTestFeedClient(AtomFeedProperties atomFeedProperties, String feedName, EventWorker eventWorker) {
        String uri = atomFeedProperties.getProperty(feedName);
        try {
            return new AtomFeedClientBuilder().
                    forFeedAt(new URI(uri)).
                    processedBy(eventWorker).
                    usingConnectionProvider(new OpenElisConnectionProvider()).
                    build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(String.format("Is not a valid URI - %s", uri));
        }
    }

    public AtomFeedClient getMRSPatientFeedClient(AtomFeedProperties atomFeedProperties, String feedName,
                                                  String authenticationURI, WebClient authenticatedWebClient) {
        PatientFeedWorker patientFeedWorker = new PatientFeedWorker(authenticatedWebClient,
                getURLPrefix(atomFeedProperties, authenticationURI));
        String uri = atomFeedProperties.getProperty(feedName);
        try {
            org.ict4h.atomfeed.client.factory.AtomFeedProperties feedProperties = new org.ict4h.atomfeed.client.factory.AtomFeedProperties();
            feedProperties.setConnectTimeout(Integer.parseInt(atomFeedProperties.getFeedConnectionTimeout()));
            feedProperties.setReadTimeout(Integer.parseInt(atomFeedProperties.getFeedReplyTimeout()));
            feedProperties.setControlsEventProcessing(true);

            return new AtomFeedClientBuilder().
                    forFeedAt(new URI(uri)).
                    processedBy(patientFeedWorker).
                    usingConnectionProvider(new OpenElisConnectionProvider()).
                    with(feedProperties).
                    build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(String.format("Is not a valid URI - %s", uri));
        }
    }

    public WebClient getAuthenticatedOpenMRSWebClient(String authenticationURI, String userName, String password,
                                                      String connectTimeoutStr, String readTimeoutStr) {
        int connectTimeout = Integer.parseInt(connectTimeoutStr);
        int readTimeout = Integer.parseInt(readTimeoutStr);

        OpenMRSAuthenticator openMRSAuthenticator = new OpenMRSAuthenticator(authenticationURI, connectTimeout, readTimeout);
        OpenMRSAuthenticationResponse authenticationResponse = openMRSAuthenticator.authenticate(userName, password, ObjectMapperRepository.objectMapper);

        if (!authenticationResponse.isAuthenticated()) throw new FeedException("Failed to authenticate with OpenMRS");

        return new WebClient(connectTimeout, readTimeout, "JSESSIONID", authenticationResponse.getSessionId());
    }

    private static String getURLPrefix(AtomFeedProperties atomFeedProperties, String authenticationURI) {
        String openMRSAuthURI = atomFeedProperties.getProperty(authenticationURI);
        URL openMRSAuthURL;
        try {
            openMRSAuthURL = new URL(openMRSAuthURI);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Is not a valid URI - " + openMRSAuthURI);
        }
        return String.format("%s://%s", openMRSAuthURL.getProtocol(), openMRSAuthURL.getAuthority());
    }
}