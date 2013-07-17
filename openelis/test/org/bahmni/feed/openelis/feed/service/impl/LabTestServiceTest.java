package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.valueholder.Test;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LabTestServiceTest {

    @Mock
    TestDAO testDao;
    @Mock
    ExternalReferenceDao externalReferenceDao;
    static final String EVENT_CONTENT = " {\"category\": \"Test\",\"description\": \"Test Panel\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";
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
        when(externalReferenceDao.getData("193", "Test")).thenReturn(null);

        LabObject labObject = new LabObject("193","Lab Test","lab test desc","1", "Test");
        Test test = new Test();
        test.setTestName("Lab Test");
        test.setDescription("lab test desc");


        labTest.saveLabObject(labObject);


        verify(testDao).insertData(test);
    }

    @org.junit.Test
    public void shouldUpdateIfExternalReferenceFound() throws IOException {
        ExternalReference reference = new ExternalReference();
        reference.setItemId(293);
        reference.setExternalId("193");

        LabObject labObject = new LabObject("193","Lab Test","lab test desc","1", "Test");
        Test test = new Test();
        test.setTestName("Lab Test");
        test.setDescription("lab test desc");


        when(externalReferenceDao.getData("193", "Test")).thenReturn(reference);
        when(testDao.getActiveTestById(293)).thenReturn(test);



        labTest.saveLabObject(labObject);

//        verify(testDao).updateData(test);
    }
}
