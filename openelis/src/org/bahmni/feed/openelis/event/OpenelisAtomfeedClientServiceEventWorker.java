package org.bahmni.feed.openelis.event;


import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.event.object.LabObject;
import org.bahmni.feed.openelis.event.service.LabService;
import org.bahmni.feed.openelis.event.service.LabTestServiceFactory;
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
            LabService labService =  LabTestServiceFactory.getLabTestService(event, new AtomFeedProperties());
            LabObject labObject =  LabTestServiceFactory.getLabObject(event, new AtomFeedProperties());
            labService.save(labObject);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



}
