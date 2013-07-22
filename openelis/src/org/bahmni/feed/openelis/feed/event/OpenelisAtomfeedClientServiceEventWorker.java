package org.bahmni.feed.openelis.feed.event;


import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.bahmni.feed.openelis.feed.service.LabService;
import org.bahmni.feed.openelis.feed.service.LabTestServiceFactory;
import org.bahmni.feed.openelis.utils.AtomfeedClientUtils;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;

import java.io.IOException;
import java.util.HashMap;

public class OpenelisAtomfeedClientServiceEventWorker extends MyEventWorker {

    public void process(Event event) {
        try {
            LabObject labObject = getLabObject(event);
            LabService labService = LabTestServiceFactory.getLabTestService(labObject.getCategory(), AtomFeedProperties.getInstance());
            labService.save(labObject);
        } catch (Exception e) {
            throw new LIMSRuntimeException(e);
        }
    }

    private LabObject getLabObject(Event event) throws IOException {
        HashMap<String, Object> paramMap = objectMapper.readValue(event.getContent(), HashMap.class);
        LabObject lab = new LabObject();
        lab.setName((String) paramMap.get("name"));
        String desc = (String) paramMap.get("description");
        if (desc == null || desc.isEmpty()) {
            desc = (String) paramMap.get("name");
        }
        lab.setDescription(desc);
        lab.setExternalId(String.valueOf(paramMap.get("id")));
        lab.setSysUserId(AtomfeedClientUtils.getSysUserId());
        lab.setCategory((String) paramMap.get("category"));
        return lab;
    }


}
