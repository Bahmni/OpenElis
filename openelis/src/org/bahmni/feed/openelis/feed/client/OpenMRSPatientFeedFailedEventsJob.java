package org.bahmni.feed.openelis.feed.client;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class OpenMRSPatientFeedFailedEventsJob extends OpenMRSFeedReaderJob {
    private static Logger logger = Logger.getLogger(OpenMRSPatientFeedFailedEventsJob.class);

    public OpenMRSPatientFeedFailedEventsJob() {
        super(logger);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            atomFeedClient.processFailedEvents();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}