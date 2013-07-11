package org.bahmni.feed.openelis.feed.client;


import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.event.EventWorkerFactory;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.datasource.WebClient;
import org.ict4h.atomfeed.client.repository.jdbc.AllFailedEventsJdbcImpl;
import org.ict4h.atomfeed.client.repository.jdbc.AllMarkersJdbcImpl;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.ict4h.atomfeed.jdbc.PropertiesJdbcConnectionProvider;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.net.URI;

public class OpeneERPLabTestFeedClient implements Job {

    private AtomFeedClient atomFeedClient;
    private AtomFeedProperties atomFeedProperties;
    private EventWorkerFactory workerFactory;

    private String feedName;
    Logger logger = Logger.getLogger(OpeneERPLabTestFeedClient.class);


    OpeneERPLabTestFeedClient(AtomFeedProperties atomFeedProperties, AtomFeedClient atomFeedClient, EventWorkerFactory workerFactory) {
        this.workerFactory = workerFactory;
        this.atomFeedProperties = atomFeedProperties;
        this.atomFeedClient = atomFeedClient;
        this.workerFactory = new EventWorkerFactory();

    }

    public OpeneERPLabTestFeedClient() {
            this(new AtomFeedProperties(), getFeedClient(), new EventWorkerFactory());
    }

    public OpeneERPLabTestFeedClient(AtomFeedProperties atomFeedProperties) {
        this(atomFeedProperties, getFeedClient(), new EventWorkerFactory());
    }

    private static AtomFeedClient getFeedClient() {
        JdbcConnectionProvider jdbcConnectionProvider = new PropertiesJdbcConnectionProvider();
        AllMarkersJdbcImpl allMarkersJdbc = new AllMarkersJdbcImpl(jdbcConnectionProvider);
        return new AtomFeedClient(new AllFeeds(new WebClient()),allMarkersJdbc, new AllFailedEventsJdbcImpl(jdbcConnectionProvider));
    }

    public void processFeed()  {
        EventWorker eventWorker = workerFactory.getWorker(EventWorkerFactory.OPENERP_ATOMFEED_WORKER, atomFeedProperties.getFeedUri(feedName));
        try {
            logger.info("Processing Lab test Feed "+ DateTime.now());
            atomFeedClient.processEvents(new URI(atomFeedProperties.getFeedUri(feedName)), eventWorker);
        } catch (Exception e) {
            logger.error("failed lab test feed execution " + e);
        }
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            processFeed();
    }

}
