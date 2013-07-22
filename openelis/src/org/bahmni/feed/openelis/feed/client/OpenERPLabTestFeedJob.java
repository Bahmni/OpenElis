package org.bahmni.feed.openelis.feed.client;


import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.event.OpenelisAtomfeedClientServiceEventWorker;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class OpenERPLabTestFeedJob implements Job {
    private AtomFeedClient atomFeedClient;
    private AtomFeedProperties atomFeedProperties;

    private static final String FEED_NAME = "openerp.labtest.feed.uri";
    private static Logger logger = Logger.getLogger(OpenERPLabTestFeedJob.class);

    OpenERPLabTestFeedJob(AtomFeedProperties atomFeedProperties, AtomFeedClient atomFeedClient) {
        this.atomFeedProperties = atomFeedProperties;
        this.atomFeedClient = atomFeedClient;
    }

    public OpenERPLabTestFeedJob() {
        this(AtomFeedProperties.getInstance(),
                AtomFeedClientFactory.getERPLabTestFeedClient(AtomFeedProperties.getInstance(), FEED_NAME,
                        new OpenelisAtomfeedClientServiceEventWorker()));
    }

    public void processFeed() {
        try {
            logger.info(String.format("Processing Lab test Feed %s", DateTime.now()));
            atomFeedClient.processEvents();
        } catch (Exception e) {
            logger.error("failed lab test feed execution " + e, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        processFeed();
    }
}
