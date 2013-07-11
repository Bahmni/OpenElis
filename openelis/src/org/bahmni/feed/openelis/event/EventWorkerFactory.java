package org.bahmni.feed.openelis.event;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.ict4h.atomfeed.client.service.EventWorker;

public class EventWorkerFactory {
    private AtomFeedProperties atomFeedProperties;
    public static final String OPENERP_ATOMFEED_WORKER =  "openerp.lab.tests.worker";

    public EventWorker getWorker(String workerName, String feedUrl) {
        if(workerName.equals(OPENERP_ATOMFEED_WORKER))
            return new OpenelisAtomfeedClientServiceEventWorker(feedUrl);
        return new EmptyWorker();
    }
}
