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
import org.bahmni.feed.openelis.feed.event.EncounterFeedWorker;
import org.bahmni.webclients.WebClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

@DisallowConcurrentExecution
public class OpenMRSEncounterFeedReaderJob extends OpenMRSFeedReaderJob {
    private static Logger logger = Logger.getLogger(OpenMRSEncounterFeedReaderJob.class);

    public OpenMRSEncounterFeedReaderJob() {
        super(logger);
    }

    @Override
    protected void doExecute(JobExecutionContext jobExecutionContext) {
        processEvents(jobExecutionContext);
    }

    @Override
    protected EventWorker createWorker(WebClient authenticatedWebClient, String urlPrefix) {
        return new EncounterFeedWorker(authenticatedWebClient, urlPrefix);
    }

    @Override
    protected String getFeedName() {
        return FeedNames.OPENMRS_ENCOUNTER_FEED_NAME;
    }
}
