/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

package org.bahmni.feed.openelis.feed.client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.webclients.ClientCookies;
import org.bahmni.webclients.HttpClient;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.quartz.JobExecutionContext;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class OpenMRSFeedReaderJob extends OpenELISFeedReaderJob {
    private static final String AUTH_URI = "openmrs.auth.uri";
    private static final String OPENMRS_USER = "openmrs.user";
    private static final String OPENMRS_PASSWORD = "openmrs.password";
    private static final String OPENMRS_WEBCLIENT_CONNECT_TIMEOUT = "openmrs.connectionTimeoutInMilliseconds";
    private static final String OPENMRS_WEBCLIENT_READ_TIMEOUT = "openmrs.replyTimeoutInMilliseconds";

    private static Map<Class, AtomFeedClient> atomFeedClients = new HashMap<>();

    protected OpenMRSFeedReaderJob(Logger logger) {
        super(logger);
    }

    protected AtomFeedClient createAtomFeedClient(AtomFeedProperties atomFeedProperties, AtomFeedClientFactory atomFeedClientFactory) {
        HttpClient authenticatedWebClient = getWebClient(atomFeedProperties, atomFeedClientFactory);
        String urlString = getURLPrefix(atomFeedProperties, AUTH_URI);
        ClientCookies cookies = getCookies(authenticatedWebClient, urlString);
        String feedName = getFeedName();
        EventWorker eventWorker = createWorker(authenticatedWebClient, urlString);
        return atomFeedClientFactory.getMRSFeedClient(atomFeedProperties,
                feedName, eventWorker, cookies);
    }

    private ClientCookies getCookies(HttpClient authenticatedWebClient, String urlString) {
        try {
            return authenticatedWebClient.getCookies(new URI(urlString));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Is not a valid URI - " + urlString);
        }
    }

    protected abstract EventWorker createWorker(HttpClient authenticatedWebClient, String urlPrefix);

    protected abstract String getFeedName();

    private HttpClient getWebClient(AtomFeedProperties atomFeedProperties, AtomFeedClientFactory atomFeedClientFactory) {
        return atomFeedClientFactory.getAuthenticatedOpenMRSWebClient(
                atomFeedProperties.getProperty(AUTH_URI),
                atomFeedProperties.getProperty(OPENMRS_USER),
                atomFeedProperties.getProperty(OPENMRS_PASSWORD),
                atomFeedProperties.getProperty(OPENMRS_WEBCLIENT_CONNECT_TIMEOUT),
                atomFeedProperties.getProperty(OPENMRS_WEBCLIENT_READ_TIMEOUT)
        );
    }

    @Override
    protected void handleException(Throwable e) {
        if (e != null && ExceptionUtils.getStackTrace(e).contains("HTTP response code: 401")) {

            initializeAtomFeedClient();
        }
    }

    private void initializeAtomFeedClient() {
        atomFeedClients.put(this.getClass(), createAtomFeedClient(AtomFeedProperties.getInstance(), new AtomFeedClientFactory()));
    }

    protected void processEvents(JobExecutionContext jobExecutionContext) {
        if (atomFeedClients.get(this.getClass()) == null)
            initializeAtomFeedClient();
        AtomFeedClient atomFeedClient = atomFeedClients.get(this.getClass());
        atomFeedClient.processEvents();
    }

    protected void processFailedEvents(JobExecutionContext jobExecutionContext) {
        if (atomFeedClients.get(this.getClass()) == null)
            initializeAtomFeedClient();
        AtomFeedClient atomFeedClient = atomFeedClients.get(this.getClass());
        atomFeedClient.processFailedEvents();
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