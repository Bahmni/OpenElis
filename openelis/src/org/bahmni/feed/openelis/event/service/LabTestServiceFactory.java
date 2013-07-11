package org.bahmni.feed.openelis.event.service;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.event.object.LabObject;
import org.bahmni.feed.openelis.event.service.impl.EmptyService;
import org.bahmni.feed.openelis.event.service.impl.LabPanelService;
import org.bahmni.feed.openelis.event.service.impl.LabTestService;
import org.bahmni.feed.openelis.utils.AtomfeedClientUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.ict4h.atomfeed.client.domain.Event;

import java.io.IOException;
import java.util.HashMap;


public class LabTestServiceFactory {

    public static LabService getLabTestService(Event event, AtomFeedProperties properties) throws IOException {
        String type =    getCategory(event);
        if(properties.getProductTypeLabTest().equals(type)){
            return  new LabTestService();
        }
        else if(properties.getProductTypePanel().equals(type)){
            return  new LabPanelService();
        }
        return  new EmptyService();
    }

    private static String getCategory(Event event) throws IOException {
        HashMap<String,Object> paramMap = new ObjectMapper().readValue(event.getContent(), HashMap.class) ;
        return (String) paramMap.get("category");
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
        return lab;
    }
}
