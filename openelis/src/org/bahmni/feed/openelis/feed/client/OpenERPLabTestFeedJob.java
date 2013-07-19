package org.bahmni.feed.openelis.feed.client;


import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.event.OpenelisAtomfeedClientServiceEventWorker;
import org.hibernate.Transaction;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.net.URI;

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
        this(AtomFeedProperties.getInstance(), AtomFeedClientFactory.getFeedClient());
    }

    public void processFeed() {
        Transaction transaction = HibernateUtil.getSession().beginTransaction();
        try {
            OpenelisAtomfeedClientServiceEventWorker eventWorker = new OpenelisAtomfeedClientServiceEventWorker(atomFeedProperties.getProperty(FEED_NAME));
            logger.info(String.format("Processing Lab test Feed %s", DateTime.now()));
            atomFeedClient.processEvents(new URI(atomFeedProperties.getProperty(FEED_NAME)), eventWorker);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("failed lab test feed execution " + e);
        } finally {
            HibernateUtil.getSession().flush();
            HibernateUtil.getSession().clear();
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        processFeed();
    }
}
