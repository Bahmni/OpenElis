package org.bahmni.feed.openelis.feed.client;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.event.LabTestFeedEventWorker;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.quartz.Job;

public abstract class OpenERPFeedReaderJob implements Job {
    protected AtomFeedClient atomFeedClient;
    private static final String FEED_NAME = "openerp.labtest.feed.uri";

    protected OpenERPFeedReaderJob(AtomFeedClient atomFeedClient) {
        this.atomFeedClient = atomFeedClient;
    }

    protected OpenERPFeedReaderJob() {
        this(AtomFeedClientFactory.getERPLabTestFeedClient(AtomFeedProperties.getInstance(), FEED_NAME,
                new LabTestFeedEventWorker()));
    }
}