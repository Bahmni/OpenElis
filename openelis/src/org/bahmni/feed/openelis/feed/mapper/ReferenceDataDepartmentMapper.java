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

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataDepartment;

import java.io.IOException;

public class ReferenceDataDepartmentMapper {
    private ObjectMapper objectMapper;

    public ReferenceDataDepartmentMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ReferenceDataDepartment map(String json) throws IOException, JsonMappingException {
        return objectMapper.readValue(json, ReferenceDataDepartment.class);
    }
}
