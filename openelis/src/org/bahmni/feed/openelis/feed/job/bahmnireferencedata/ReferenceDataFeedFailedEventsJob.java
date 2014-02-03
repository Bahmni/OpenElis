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

package org.bahmni.feed.openelis.feed.job.bahmnireferencedata;

import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.event.ReferenceDataFeedEventWorker;
import org.bahmni.feed.openelis.feed.job.OpenELISFeedFailedEventsJob;
import org.bahmni.webclients.AnonymousAuthenticator;
import org.bahmni.webclients.Authenticator;
import org.bahmni.webclients.ConnectionDetails;
import org.bahmni.webclients.HttpClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class ReferenceDataFeedFailedEventsJob extends OpenELISFeedFailedEventsJob {

    private static final String REFERENCE_DATA_WEBCLIENT_CONNECT_TIMEOUT = "openerp.connectionTimeoutInMilliseconds";
    private static final String REFERENCE_DATA_WEBCLIENT_READ_TIMEOUT = "openerp.replyTimeoutInMilliseconds";

    private static final String FEED_NAME = "reference.data.feed.uri";


    protected static Logger logger = Logger.getLogger(ReferenceDataFeedReaderJob.class);

    public ReferenceDataFeedFailedEventsJob() {
        super(logger);
    }

    @Override
    protected String getFeedName() {
        return FEED_NAME;
    }

    @Override
    protected ConnectionDetails getConnectionDetails() {
        AtomFeedProperties atomFeedProperties = AtomFeedProperties.getInstance();

        return new ConnectionDetails(atomFeedProperties.getProperty(getFeedName()),
                null, null,
                Integer.parseInt(atomFeedProperties.getProperty(REFERENCE_DATA_WEBCLIENT_CONNECT_TIMEOUT)),
                Integer.parseInt(atomFeedProperties.getProperty(REFERENCE_DATA_WEBCLIENT_READ_TIMEOUT)));
    }

    @Override
    protected EventWorker createWorker(HttpClient authenticatedWebClient, String urlPrefix) {
        return new ReferenceDataFeedEventWorker(authenticatedWebClient, urlPrefix);
    }

    protected Authenticator getAuthenticator(ConnectionDetails connectionDetails) {
        return new AnonymousAuthenticator(connectionDetails);
    }
}
