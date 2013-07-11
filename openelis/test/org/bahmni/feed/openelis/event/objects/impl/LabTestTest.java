package org.bahmni.feed.openelis.event.objects.impl;

import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.valueholder.Test;

import java.io.IOException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LabTestTest {

    @Mock
    TestDAO testDao;
    @Mock
    ExternalReferenceDao externalReferenceDao;
    static final String EVENT_CONTENT = " {\"category\": \"test\",\"description\": \"Test Panel\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";
    LabTestService labTest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        labTest = new LabTestService(testDao,externalReferenceDao);

    }

    @After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void shouldInsertNewIfExternalReferenceNotFound() throws IOException {
        when(externalReferenceDao.getData(anyString())).thenReturn(null);

        Event event = new Event("554433221",EVENT_CONTENT);
        Test test = new Test();
        test.setTestName("ECHO");
        test.setDescription("Test Panel");


        labTest.saveEvent(event);


        verify(testDao).insertData(test);
    }

    @org.junit.Test
    public void shouldUpdateIfExternalReferenceFound() throws IOException {
        ExternalReference reference = new ExternalReference();
        reference.setItemId(293);
        reference.setExternalId("193");

        Test test = new Test();
        test.setTestName("ECHO");
        test.setDescription("Test Panel");

        when(externalReferenceDao.getData("193")).thenReturn(reference);
        when(testDao.getActiveTestById(293)).thenReturn(test);

        Event event = new Event("554433221",EVENT_CONTENT);

        labTest.saveEvent(event);

        verify(testDao).updateData(test);
    }
}
