package org.bahmni.feed.openelis.event;


import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.event.objects.EventObject;
import org.bahmni.feed.openelis.event.objects.EventObjectFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.io.IOException;
import java.util.HashMap;

public class OpenelisAtomfeedClientServiceEventWorker implements EventWorker {

    private String feedUrl;

    public OpenelisAtomfeedClientServiceEventWorker(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public void process(Event event) {
        try {
            EventObject eventObject =  EventObjectFactory.getEventObjectInstance(getCategory(event), new AtomFeedProperties());;
            eventObject.save(event);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String getCategory(Event event) throws IOException {
        HashMap<String,Object> paramMap = new ObjectMapper().readValue(event.getContent(), HashMap.class) ;
        return (String) paramMap.get("category");
    }

}
