package org.bahmni.feed.openelis.feed.event;

import org.ict4h.atomfeed.client.service.EventWorker;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;

public class EventWorkerFactory {
    public static final String OPENERP_ATOMFEED_WORKER = "openerp.lab.tests.worker";

    public EventWorker getWorker(String workerName, String feedUrl) {
        if (workerName.equals(OPENERP_ATOMFEED_WORKER))
            return new OpenelisAtomfeedClientServiceEventWorker(feedUrl);
        throw new LIMSRuntimeException(String.format("No worker configured for the worker by name %s", workerName));
    }
}
