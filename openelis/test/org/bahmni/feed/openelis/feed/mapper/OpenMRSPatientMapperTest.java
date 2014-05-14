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

import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPatient;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class OpenMRSPatientMapperTest extends OpenMRSMapperBaseTest {
    @Test
    public void map() throws IOException {
        String json = deserialize("sampleOpenMRSPatient.json");
        OpenMRSPatientMapper openMRSPatientMapper = new OpenMRSPatientMapper(ObjectMapperForTest.MAPPER);
        OpenMRSPatient openMRSPatient = openMRSPatientMapper.map(json);
        Assert.assertNotNull(openMRSPatient);
    }
}
