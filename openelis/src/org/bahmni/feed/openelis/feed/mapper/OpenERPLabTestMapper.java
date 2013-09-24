/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

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
