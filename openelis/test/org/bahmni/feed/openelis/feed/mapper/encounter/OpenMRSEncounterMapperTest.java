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

package org.bahmni.feed.openelis.feed.mapper.encounter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSConcept;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSConceptName;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSEncounter;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSOrder;
import org.bahmni.feed.openelis.feed.mapper.ObjectMapperForTest;
import org.bahmni.feed.openelis.feed.mapper.OpenMRSMapperBaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class OpenMRSEncounterMapperTest extends OpenMRSMapperBaseTest {
    @Test
    public void map() throws IOException {
        String json = deserialize("sampleOpenMRSEncounter.json");
        OpenMRSEncounterMapper openMRSEncounterMapper = new OpenMRSEncounterMapper(getObjectMapperThatAllowsComments());
        OpenMRSEncounter openMRSEncounter = openMRSEncounterMapper.map(json);
        Assert.assertNotNull(openMRSEncounter);

        Assert.assertEquals("7820b07d-50e9-4fed-b991-c38692b3d4ec", openMRSEncounter.getEncounterUuid());
        Assert.assertEquals(3, openMRSEncounter.getOrders().size());

        Assert.assertEquals("105059a8-5226-4b1f-b512-0d3ae685287d", openMRSEncounter.getPatientUuid());

        checkOrder(openMRSEncounter.getOrders().get(0), "ac0819a9-11c1-4310-8f0a-feee71e5086b", "4e167df9-80f5-4caa-9a82-ec97908cbd59", "Anaemia Panel", true);
        checkOrder(openMRSEncounter.getOrders().get(1), "ce3eeba1-2176-48d0-9ff4-53b731121274", "8c47a469-1b8f-47d9-9363-b97b45e3e740", "Routine Blood", true);
        checkOrder(openMRSEncounter.getOrders().get(2), "3508dbea-6844-4174-8787-40ab809f2522", "15a9d9e5-edcb-4e64-85ad-551693aeed5c", "ESR", false);
    }

    private void checkOrder(OpenMRSOrder openMRSOrder, String expectedOrderUUID, String expectedTestOrPanelUUID, String testOrPanelName, boolean isPanel) {
        Assert.assertEquals(expectedOrderUUID, openMRSOrder.getUuid());
        Assert.assertEquals("Lab Order", openMRSOrder.getOrderType());

        OpenMRSConcept concept = openMRSOrder.getConcept();
        Assert.assertEquals(expectedTestOrPanelUUID, concept.getUuid());
        if (isPanel)
            Assert.assertTrue("This is a set. Panel.", concept.isSet());
        else
            Assert.assertFalse("This is not a set. Test.", concept.isSet());

        Assert.assertEquals(expectedTestOrPanelUUID, openMRSOrder.getTestOrPanelUUID());

        OpenMRSConceptName conceptName = concept.getName();
        Assert.assertEquals(testOrPanelName, conceptName.getName());
    }

    private ObjectMapper getObjectMapperThatAllowsComments() {
        ObjectMapper mapper = ObjectMapperForTest.MAPPER;
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        return mapper;
    }

}
