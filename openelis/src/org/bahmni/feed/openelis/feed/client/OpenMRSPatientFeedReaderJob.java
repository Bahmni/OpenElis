package org.bahmni.feed.openelis.feed.client;

import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class OpenMRSPatientFeedReaderJob extends OpenMRSFeedReaderJob {
    private static Logger logger = Logger.getLogger(OpenMRSPatientFeedReaderJob.class);
    protected static AtomFeedClient atomFeedClient;

    public OpenMRSPatientFeedReaderJob() {
        super(logger);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            if (atomFeedClient == null)
                atomFeedClient = createAtomFeedClient(AtomFeedProperties.getInstance(), new AtomFeedClientFactory());
            atomFeedClient.processEvents();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}