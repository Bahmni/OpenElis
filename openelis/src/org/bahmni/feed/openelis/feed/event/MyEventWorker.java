package org.bahmni.feed.openelis.feed.event;

import org.codehaus.jackson.map.ObjectMapper;
import org.ict4h.atomfeed.client.service.EventWorker;

public abstract class MyEventWorker implements EventWorker {
    protected static ObjectMapper objectMapper = new ObjectMapper();
}