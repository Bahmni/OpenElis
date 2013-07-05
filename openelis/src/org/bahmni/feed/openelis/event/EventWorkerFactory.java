package org.bahmni.feed.openelis.event;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.ict4h.atomfeed.client.service.EventWorker;

public class EventWorkerFactory {
    private AtomFeedProperties atomFeedProperties;


    public static final String OPENELIS_ATOMFEED_SERVICE =  "openelis.openerp.sync.objects";
    public EventWorker getWorker(String workerName, String feedUrl) {
        if(workerName.equals(OPENELIS_ATOMFEED_SERVICE))
            return new OpenelisAtomfeedClientServiceEventWorker(feedUrl);
        return new EmptyWorker();
    }
}
