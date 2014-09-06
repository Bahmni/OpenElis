package org.bahmni.feed.openelis.feed.job.openmrs;

import org.bahmni.feed.openelis.feed.event.LabFeedEventWorker;
import org.bahmni.feed.openelis.feed.job.FeedNames;
import org.bahmni.webclients.HttpClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class OpenMRSLabFeedReaderJob extends OpenMRSFeedReaderJob{

    @Override
    protected EventWorker createWorker(HttpClient authenticatedWebClient, String urlPrefix) {
        return new LabFeedEventWorker(authenticatedWebClient, urlPrefix);
    }

    @Override
    protected String getFeedName() {
        return FeedNames.OPENMRS_LAB_FEED_NAME;
    }
}
