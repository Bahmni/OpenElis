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
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class OpenERPLabTestFailedEventsJob extends OpenERPFeedReaderJob {
    private static Logger logger = Logger.getLogger(OpenERPLabTestFailedEventsJob.class);
    private static final String FEED_NAME = "openerp.labtest.feed.uri";

    protected OpenERPLabTestFailedEventsJob(Logger logger) {
        super(logger);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            if(atomFeedClient == null){
                initializeERPAtomFeedClient();
            }
            logger.info("Started");
            atomFeedClient.processFailedEvents();
        } catch (Exception e) {
            logger.error("Failed", e);
        }
    }

    @Override
    protected String getFeedName() {
        return FEED_NAME;
    }
}
