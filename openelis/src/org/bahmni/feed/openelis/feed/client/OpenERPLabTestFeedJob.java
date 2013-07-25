package org.bahmni.feed.openelis.feed.client;


import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.event.LabTestFeedEventWorker;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;

public class OpenERPLabTestFeedJob implements Job {
    private AtomFeedClient atomFeedClient;

    private static final String FEED_NAME = "openerp.labtest.feed.uri";
    private static Logger logger = Logger.getLogger(OpenERPLabTestFeedJob.class);

    OpenERPLabTestFeedJob(AtomFeedClient atomFeedClient) {
        this.atomFeedClient = atomFeedClient;
    }

    public OpenERPLabTestFeedJob() {
        this(
                AtomFeedClientFactory.getERPLabTestFeedClient(AtomFeedProperties.getInstance(), FEED_NAME,
                        new LabTestFeedEventWorker(new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl()))));
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
