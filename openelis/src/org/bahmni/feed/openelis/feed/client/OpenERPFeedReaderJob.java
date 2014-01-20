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

import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.event.LabTestFeedEventWorker;
import org.bahmni.webclients.ClientCookies;
import org.bahmni.webclients.HttpClient;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.quartz.*;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class OpenERPFeedReaderJob implements Job {
    private static final String OPENERP_WEBCLIENT_CONNECT_TIMEOUT = "openerp.connectionTimeoutInMilliseconds";
    private static final String OPENERP_WEBCLIENT_READ_TIMEOUT = "openerp.replyTimeoutInMilliseconds";
    private final Logger logger;
    protected AtomFeedClient atomFeedClient;

    private AtomFeedProperties atomFeedProperties;

    protected OpenERPFeedReaderJob(Logger logger){
        this.logger = logger;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            if(atomFeedClient == null){
                initializeERPAtomFeedClient();
            }
            logger.info("Started");

            processEvents();

            logger.info("Successfully completed");
        } catch (Exception e) {
            logger.error("Failed", e);
        }
    }

    protected abstract void processEvents();


    protected void initializeERPAtomFeedClient() {

        atomFeedProperties = AtomFeedProperties.getInstance();
        ClientCookies cookies;
        try {
            cookies = getWebClient().getCookies(new URI(atomFeedProperties.getProperty(getFeedName())));
        } catch (URISyntaxException e) {
            logger.error("Unable to get cookies");
            cookies = new ClientCookies();
        }
        atomFeedClient = new AtomFeedClientFactory().getERPLabTestFeedClient(AtomFeedProperties.getInstance(), getFeedName(),
                new LabTestFeedEventWorker(),cookies);
    }

    protected abstract String getFeedName();

    private HttpClient getWebClient() {
        AtomFeedClientFactory atomFeedClientFactory = new AtomFeedClientFactory();
        return atomFeedClientFactory.getOpenERPWebClient(
                atomFeedProperties.getProperty(getFeedName()),
                atomFeedProperties.getProperty(OPENERP_WEBCLIENT_CONNECT_TIMEOUT),
                atomFeedProperties.getProperty(OPENERP_WEBCLIENT_READ_TIMEOUT)
        );
    }
}
