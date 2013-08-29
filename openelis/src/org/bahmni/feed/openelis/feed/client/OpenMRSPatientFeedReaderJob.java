package org.bahmni.feed.openelis.feed.client;

import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

@DisallowConcurrentExecution
public class OpenMRSPatientFeedReaderJob extends OpenMRSFeedReaderJob {
    private static Logger logger = Logger.getLogger(OpenMRSPatientFeedReaderJob.class);
    protected static AtomFeedClient atomFeedClient;

    public OpenMRSPatientFeedReaderJob() {
        super(logger);
    }

    public OpenMRSPatientFeedReaderJob(AtomFeedClient atomFeedClient) {
        super(logger);
        OpenMRSPatientFeedReaderJob.atomFeedClient = atomFeedClient;
    }

    @Override
    protected void doExecute(JobExecutionContext jobExecutionContext) {
        if (atomFeedClient == null)
            atomFeedClient = createAtomFeedClient(AtomFeedProperties.getInstance(), new AtomFeedClientFactory());
        atomFeedClient.processEvents();
    }

    @Override
    protected void reInitializeAtomFeedClient() {
        atomFeedClient = createAtomFeedClient(AtomFeedProperties.getInstance(), new AtomFeedClientFactory());
    }
}