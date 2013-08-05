package org.bahmni.feed.openelis.feed.mapper;

import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.ict4h.atomfeed.client.domain.Event;

import java.io.IOException;
import java.util.HashMap;

public class OpenERPLabTestMapper {

    public LabObject getLabObject(Event event, String sysUserId) throws IOException {
            HashMap<String, Object> paramMap = ObjectMapperRepository.objectMapper.readValue(event.getContent(), HashMap.class);
            LabObject lab = new LabObject();
            lab.setName((String) paramMap.get("name"));
            String desc = (String) paramMap.get("name");
            lab.setDescription(desc);
            lab.setExternalId((String)paramMap.get("uuid"));
            lab.setSysUserId(sysUserId);
            lab.setCategory((String) paramMap.get("category"));
            lab.setStatus((String) paramMap.get("status"));
            return lab;
        }

}
