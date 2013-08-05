package org.bahmni.feed.openelis.feed.mapper;

import junit.framework.Assert;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Test;

public class OpenERPLabTestMapperTest {
    static final String TEST_EVENT_CONTENT = " {\"category\": \"Test\",\"uuid\": \"07a5f352-ad6e-4638-9c99-2d5af364a920\", \"list_price\": \"0.0\", \"name\": \"ECHO\",\"status\": \"active\", \"type\": \"service\", \"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";

    @Test
    public void shouldMapToTestObject() throws Exception {
        OpenERPLabTestMapper labTestMapper = new OpenERPLabTestMapper();
        LabObject labObject = labTestMapper.getLabObject(new Event("tag:atomfeed.ict4h.org:05a68ba2-6764-4a31-b28e-21ddfc445e8a", TEST_EVENT_CONTENT), "1");
        Assert.assertEquals("Test",labObject.getCategory());
        Assert.assertEquals("ECHO",labObject.getName());
        Assert.assertEquals("07a5f352-ad6e-4638-9c99-2d5af364a920",labObject.getExternalId());
        Assert.assertEquals("active",labObject.getStatus());

    }
}
