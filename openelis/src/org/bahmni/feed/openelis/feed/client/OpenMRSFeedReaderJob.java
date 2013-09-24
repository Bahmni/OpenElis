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
import org.bahmni.webclients.WebClient;
import org.ict4h.atomfeed.client.service.AtomFeedClient;

import java.io.IOException;

public abstract class OpenMRSFeedReaderJob extends OpenELISFeedReaderJob {
    private static final String FEED_NAME = "openmrs.patient.feed.uri";
    private static final String AUTH_URI = "openmrs.auth.uri";
    private static final String OPENMRS_USER = "openmrs.user";
    private static final String OPENMRS_PASSWORD = "openmrs.password";
    private static final String OPENMRS_WEBCLIENT_CONNECT_TIMEOUT = "openmrs.connectionTimeoutInMilliseconds";
    private static final String OPENMRS_WEBCLIENT_READ_TIMEOUT = "openmrs.replyTimeoutInMilliseconds";

    protected OpenMRSFeedReaderJob(Logger logger) {
        super(logger);
    }

    protected AtomFeedClient createAtomFeedClient(AtomFeedProperties atomFeedProperties, AtomFeedClientFactory atomFeedClientFactory) {
        WebClient authenticatedWebClient = getWebClient(atomFeedProperties, atomFeedClientFactory);
        return atomFeedClientFactory.getMRSPatientFeedClient(atomFeedProperties,
                FEED_NAME, AUTH_URI, authenticatedWebClient);
    }

    private WebClient getWebClient(AtomFeedProperties atomFeedProperties, AtomFeedClientFactory atomFeedClientFactory) {
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
            reInitializeAtomFeedClient();
        }
    }

    protected abstract void reInitializeAtomFeedClient();
}
