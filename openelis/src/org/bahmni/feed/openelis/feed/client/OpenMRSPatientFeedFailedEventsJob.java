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
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            if (atomFeedClient == null)
                atomFeedClient = createAtomFeedClient(AtomFeedProperties.getInstance(), new AtomFeedClientFactory());
            atomFeedClient.processFailedEvents();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}