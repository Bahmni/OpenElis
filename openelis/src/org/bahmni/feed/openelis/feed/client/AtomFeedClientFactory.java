package org.bahmni.feed.openelis.feed.client;

import org.apache.commons.codec.binary.Base64;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.event.PatientFeedWorker;
import org.bahmni.feed.openelis.webclient.WebClient;
import org.ict4h.atomfeed.client.factory.AtomFeedClientBuilder;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class AtomFeedClientFactory {
    static AtomFeedClient getERPLabTestFeedClient(AtomFeedProperties atomFeedProperties, String feedName, EventWorker eventWorker)  {
        String uri = atomFeedProperties.getProperty(feedName);
        try {
            return new AtomFeedClientBuilder().
                            forFeedAt(new URI(uri)).
                            processedBy(eventWorker).
                            build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Is not a valid URI - " + uri);
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

            return new AtomFeedClientBuilder().
                            forFeedAt(new URI(uri)).
                            processedBy(patientFeedWorker).
                            with(feedProperties).
                            build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Is not a valid URI - " + uri);
        }
    }

    public WebClient getAuthenticatedWebClient(String authenticationURI, String userName, String password,
                                               String connectTimeoutStr, String readTimeoutStr) {
        int connectTimeout = Integer.parseInt(connectTimeoutStr);
        int readTimeout = Integer.parseInt(readTimeoutStr);

        WebClient webClient = new WebClient(connectTimeout, readTimeout);
        HashMap<String, String> headers = new HashMap<String, String>();
        String authorizationHeaderValue = String.format("Basic %s:%s", userName, password);
        headers.put("Authorization", new String(Base64.encodeBase64(authorizationHeaderValue.getBytes())));
        headers.put("Disable-WWW-Authenticate", "true");

        webClient.get(URI.create(authenticationURI), headers);
        return webClient;
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