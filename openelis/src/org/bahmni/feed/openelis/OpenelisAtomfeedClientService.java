package org.bahmni.feed.openelis;


import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.event.EventWorkerFactory;
import org.ict4h.atomfeed.client.factory.AtomClientFactory;
import org.ict4h.atomfeed.client.repository.jdbc.AllFailedEventsJdbcImpl;
import org.ict4h.atomfeed.client.repository.jdbc.AllMarkersJdbcImpl;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.ict4h.atomfeed.jdbc.PropertiesJdbcConnectionProvider;
import org.joda.time.DateTime;

import java.net.URI;

public class OpenelisAtomfeedClientService {

    private AtomFeedClient atomFeedClient;
    private AtomFeedProperties atomFeedProperties;
    private EventWorkerFactory workerFactory;

    private String feedName;
    Logger logger = Logger.getLogger(OpenelisAtomfeedClientService.class);


    OpenelisAtomfeedClientService(AtomFeedProperties atomFeedProperties, AtomFeedClient atomFeedClient, EventWorkerFactory workerFactory) {
        this.workerFactory = workerFactory;
        this.atomFeedProperties = atomFeedProperties;
        this.atomFeedClient = atomFeedClient;
   //     this.workerFactory = new EventWorkerFactory();

    }

    public OpenelisAtomfeedClientService(AtomFeedProperties atomFeedProperties) {
        this(atomFeedProperties, getFeedClient(), new EventWorkerFactory());
    }

    private static AtomFeedClient getFeedClient() {
        JdbcConnectionProvider jdbcConnectionProvider = new PropertiesJdbcConnectionProvider();
        AllMarkersJdbcImpl allMarkersJdbc = new AllMarkersJdbcImpl(jdbcConnectionProvider);
        return new AtomClientFactory().create(allMarkersJdbc, new AllFailedEventsJdbcImpl(jdbcConnectionProvider));
    }

    public void processFeed()  {
        EventWorker eventWorker = workerFactory.getWorker(EventWorkerFactory.OPENELIS_ATOMFEED_SERVICE, atomFeedProperties.getFeedUri(feedName));
        try {
            logger.info("Processing Customer Feed "+ DateTime.now());
            atomFeedClient.processEvents(new URI(atomFeedProperties.getFeedUri(feedName)), eventWorker);
        } catch (Exception e) {
            logger.error("failed customer feed execution " + e);
        }
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }


}
