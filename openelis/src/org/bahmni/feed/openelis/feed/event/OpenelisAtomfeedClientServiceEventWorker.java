package org.bahmni.feed.openelis.feed.event;


import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.bahmni.feed.openelis.feed.service.LabService;
import org.bahmni.feed.openelis.feed.service.LabTestServiceFactory;
import org.bahmni.feed.openelis.utils.AtomfeedClientUtils;
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
            LabObject labObject =  getLabObject(event, new AtomFeedProperties());
            LabService labService =  LabTestServiceFactory.getLabTestService(labObject.getCategory(), new AtomFeedProperties());
            labService.save(labObject);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static LabObject getLabObject(Event event,AtomFeedProperties properties ) throws IOException {
        HashMap<String,Object> paramMap = new ObjectMapper().readValue(event.getContent(), HashMap.class) ;
        LabObject lab = new LabObject();
        lab.setName((String) paramMap.get("name"));
        String desc = (String) paramMap.get("description");
        if(desc == null || desc.isEmpty()){
            desc = (String) paramMap.get("name");
        }
        lab.setDescription(desc);
        lab.setExternalId(String.valueOf( paramMap.get("id")));
        lab.setSysUserId(AtomfeedClientUtils.getSysUserId());
        lab.setCategory((String) paramMap.get("category"));
        return lab;
    }



}
