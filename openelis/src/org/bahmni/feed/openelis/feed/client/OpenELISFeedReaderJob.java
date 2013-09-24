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
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class OpenELISFeedReaderJob implements Job {
    private final Logger logger;

    protected OpenELISFeedReaderJob(Logger logger) {
        logger.info("Started");
        this.logger = logger;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            doExecute(jobExecutionContext);
        } catch (Exception e) {
            try {
                handleException(e);
            } finally {
                logger.error(e.getMessage(), e);
            }
        }
    }

    protected abstract void handleException(Throwable e);

    protected abstract void doExecute(JobExecutionContext jobExecutionContext);
}
