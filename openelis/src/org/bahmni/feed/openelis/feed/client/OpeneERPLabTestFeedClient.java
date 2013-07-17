package org.bahmni.feed.openelis.feed.client;


import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.event.EventWorkerFactory;
import org.hibernate.Transaction;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.net.URI;

public class OpeneERPLabTestFeedClient implements Job {
    private AtomFeedClient atomFeedClient;
    private AtomFeedProperties atomFeedProperties;
    private EventWorkerFactory workerFactory;

    private static final String FEED_NAME = "openerp.labtest.feed.generator.uri";
    Logger logger = Logger.getLogger(OpeneERPLabTestFeedClient.class);


    OpeneERPLabTestFeedClient(AtomFeedProperties atomFeedProperties, AtomFeedClient atomFeedClient, EventWorkerFactory workerFactory) {
        this.workerFactory = workerFactory;
        this.atomFeedProperties = atomFeedProperties;
        this.atomFeedClient = atomFeedClient;
        this.workerFactory = new EventWorkerFactory();
    }

    OpeneERPLabTestFeedClient(AtomFeedProperties atomFeedProperties) {
        this(atomFeedProperties, AtomFeedClientFactory.getFeedClient(), new EventWorkerFactory());
    }

    public OpeneERPLabTestFeedClient() {
        this(AtomFeedProperties.getInstance(), AtomFeedClientFactory.getFeedClient(), new EventWorkerFactory());
    }

    public void processFeed() {
        EventWorker eventWorker = workerFactory.getWorker(EventWorkerFactory.OPENERP_ATOMFEED_WORKER, atomFeedProperties.getFeedUri(FEED_NAME));
        Transaction transaction = HibernateUtil.getSession().beginTransaction();;
        try {
            logger.info("Processing Lab test Feed " + DateTime.now());
            atomFeedClient.processEvents(new URI(atomFeedProperties.getFeedUri(FEED_NAME)), eventWorker);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("failed lab test feed execution " + e);
        }  finally {
            HibernateUtil.getSession().flush();
            HibernateUtil.getSession().clear();
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        processFeed();
    }
}
