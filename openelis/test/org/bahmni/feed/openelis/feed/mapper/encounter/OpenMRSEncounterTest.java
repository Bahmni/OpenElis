package org.bahmni.feed.openelis.feed.mapper.encounter;

import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSConcept;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSConceptName;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSEncounter;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSOrder;
import org.bahmni.feed.openelis.feed.mapper.ObjectMapperForTest;
import org.bahmni.feed.openelis.feed.mapper.OpenMRSMapperBaseTest;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class OpenMRSEncounterTest extends OpenMRSMapperBaseTest {
    @Test
    public void map() throws IOException {
        String json = deserialize("sampleOpenMRSEncounter.json");
        OpenMRSEncounterMapper openMRSEncounterMapper = new OpenMRSEncounterMapper(getObjectMapperThatAllowsComments());
        OpenMRSEncounter openMRSEncounter = openMRSEncounterMapper.map(json);
        Assert.assertNotNull(openMRSEncounter);

        Assert.assertEquals("7a4ea494-01f3-4e28-bc96-f42960065bc4", openMRSEncounter.getUuid());
        Assert.assertEquals(1, openMRSEncounter.getOrders().size());

        OpenMRSOrder openMRSOrder = openMRSEncounter.getOrders().get(0);

        Assert.assertEquals("b6d0590a-8a18-4255-8dd6-5be84b3f9c34", openMRSOrder.getUuid());
        Assert.assertEquals("Lab Order", openMRSOrder.getOrderType().getName());
        Assert.assertFalse(openMRSOrder.getOrderType().isRetired());

        OpenMRSConcept concept = openMRSOrder.getConcept();
        Assert.assertEquals("0f609895-7d45-4b22-a91b-755d75e470ec", concept.getUuid());
        Assert.assertTrue("This is a set. Panel.", concept.isSet());

        OpenMRSConceptName conceptName = concept.getName();
        Assert.assertEquals("Routine Urine", conceptName.getName());
    }

    private ObjectMapper getObjectMapperThatAllowsComments() {
        ObjectMapper mapper = ObjectMapperForTest.MAPPER;
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        return mapper;
    }

}
