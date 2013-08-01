package org.bahmni.feed.openelis.feed.client;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class OpenERPLabTestFailedEventsJob extends OpenERPFeedReaderJob {
    private static Logger logger = Logger.getLogger(OpenERPLabTestFailedEventsJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            logger.info("Started");
            atomFeedClient.processFailedEvents();
        } catch (Exception e) {
            logger.error("Failed", e);
        }
    }
}
