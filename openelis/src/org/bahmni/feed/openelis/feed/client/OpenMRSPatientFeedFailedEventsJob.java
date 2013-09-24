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
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class OpenMRSPatientFeedFailedEventsJob extends OpenMRSFeedReaderJob {
    private static Logger logger = Logger.getLogger(OpenMRSPatientFeedFailedEventsJob.class);
    protected static AtomFeedClient atomFeedClient;

    public OpenMRSPatientFeedFailedEventsJob() throws JobExecutionException {
        super(logger);
    }

    @Override
    protected void doExecute(JobExecutionContext jobExecutionContext) {
        if (atomFeedClient == null)
            atomFeedClient = createAtomFeedClient(AtomFeedProperties.getInstance(), new AtomFeedClientFactory());
        atomFeedClient.processFailedEvents();
    }

    @Override
    protected void reInitializeAtomFeedClient() {
        atomFeedClient = createAtomFeedClient(AtomFeedProperties.getInstance(), new AtomFeedClientFactory());
    }
}
