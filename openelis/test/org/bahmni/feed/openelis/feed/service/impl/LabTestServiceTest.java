package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestSection;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LabTestServiceTest {

    @Mock
    TestDAO testDao;
    @Mock
    ExternalReferenceDao externalReferenceDao;
    @Mock
    TestSectionDAO testSectionDAO;
    static final String EVENT_CONTENT = " {\"category\": \"Test\",\"description\": \"Test Panel\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";
    LabTestService labTestService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        labTestService = new LabTestService(testDao,externalReferenceDao, testSectionDAO);

    }

    @org.junit.Test
    public void shouldInsertNewIfExternalReferenceNotFound() throws IOException {
        TestSection section = new TestSection();
        section.setTestSectionName("New");
        LabObject labObject = new LabObject("193","Lab Test","lab test desc","1", "Test","active");

        when(externalReferenceDao.getData("193", "Test")).thenReturn(null);
        when(testSectionDAO.getTestSectionByName("New")).thenReturn(section);

        labTestService.save(labObject);

        ArgumentCaptor<Test> testCaptor = ArgumentCaptor.forClass(Test.class);

        verify(testDao).insertData(testCaptor.capture());

        Test testCaptorValue = testCaptor.getValue();
        assertEquals(labObject.getName(), testCaptorValue.getTestName());
//          TODO : come back to here [Sush/ IN]
//        ArgumentCaptor<ExternalReference> externalReferenceCaptor = ArgumentCaptor.forClass(ExternalReference.class);
//
//        verify(externalReferenceDao).insertData(externalReferenceCaptor.capture());
//
//        ExternalReference externalReference = externalReferenceCaptor.getValue();
//        assertEquals(labObject.getExternalId(), externalReference.getExternalId());
//        assertEquals("Test", externalReference.getType());
    }

    @org.junit.Test
    public void shouldUpdateIfExternalReferenceFound() throws IOException {
        ExternalReference reference = new ExternalReference();
        reference.setItemId(293);
        reference.setExternalId("193");

        LabObject labObject = new LabObject("193","Lab Test","lab test desc new","1", "Test","active");
        Test test = new Test();
        test.setTestName("Lab Test");
        test.setDescription("lab test desc old");
        test.setId("293");

        when(externalReferenceDao.getData("193", "Test")).thenReturn(reference);
        when(testDao.getTestById("293")).thenReturn(test);

        labTestService.save(labObject);

        ArgumentCaptor<Test> testArgumentCaptor = ArgumentCaptor.forClass(Test.class);
        verify(testDao).updateData(testArgumentCaptor.capture());
        Test savedTest = testArgumentCaptor.getValue();
        assertEquals(labObject.getDescription(), savedTest.getDescription());
        assertEquals("293", savedTest.getId());
    }

    @org.junit.Test
    public void shouldInActivateExistingActiveTest() throws IOException {
        ExternalReference reference = new ExternalReference();
        reference.setItemId(293);
        reference.setExternalId("193");

        LabObject labObject = new LabObject("193","Lab Test","lab test desc new","1", "Test","inactive");
        Test test = new Test();
        test.setTestName("Lab Test");
        test.setDescription("lab test desc old");
        test.setId("293");
        test.setIsActive("Y");


        when(externalReferenceDao.getData("193", "Test")).thenReturn(reference);
        when(testDao.getTestById("293")).thenReturn(test);

        labTestService.save(labObject);

        ArgumentCaptor<Test> testArgumentCaptor = ArgumentCaptor.forClass(Test.class);
        verify(testDao).updateData(testArgumentCaptor.capture());
        Test savedTest = testArgumentCaptor.getValue();
        assertEquals(labObject.getDescription(), savedTest.getDescription());
        assertEquals("293", savedTest.getId());
        assertEquals("N", savedTest.getIsActive());
    }

    @org.junit.Test
    public void shouldActivateExistingInactiveTest() throws IOException {
        ExternalReference reference = new ExternalReference();
        reference.setItemId(293);
        reference.setExternalId("193");

        LabObject labObject = new LabObject("193","Lab Test","lab test desc new","1", "Test","active");
        Test test = new Test();
        test.setTestName("Lab Test");
        test.setDescription("lab test desc old");
        test.setId("293");
        test.setIsActive("N");


        when(externalReferenceDao.getData("193", "Test")).thenReturn(reference);
        when(testDao.getTestById("293")).thenReturn(test);

        labTestService.save(labObject);

        ArgumentCaptor<Test> testArgumentCaptor = ArgumentCaptor.forClass(Test.class);
        verify(testDao).updateData(testArgumentCaptor.capture());
        Test savedTest = testArgumentCaptor.getValue();
        assertEquals(labObject.getDescription(), savedTest.getDescription());
        assertEquals("293", savedTest.getId());
        assertEquals("Y", savedTest.getIsActive());
    }

    @org.junit.Test
    public void shouldSaveNewInactivateTest() throws IOException {
        TestSection section = new TestSection();
        section.setTestSectionName("New");

        LabObject labObject = new LabObject("193","Lab Test","lab test desc new","1", "Test","inactive");

        when(externalReferenceDao.getData("193", "Test")).thenReturn(null);
        when(testSectionDAO.getTestSectionByName("New")).thenReturn(section);

        labTestService.save(labObject);

        ArgumentCaptor<Test> testCaptor = ArgumentCaptor.forClass(Test.class);

        verify(testDao).insertData(testCaptor.capture());

        Test testCaptorValue = testCaptor.getValue();
        assertEquals(labObject.getName(), testCaptorValue.getTestName());
        assertEquals("N", testCaptorValue.getIsActive());
    }

    @org.junit.Test
    public void shouldDeleteTestIfNoReferencesExistToTest() throws IOException {
        ExternalReference reference = new ExternalReference(293, "193", "type");
        LabObject labObject = new LabObject("193","Lab Test","lab test desc new","1", "Test","deleted");
        Test test = new Test();
        test.setTestName("Lab Test");
        test.setDescription("lab test desc old");
        test.setId("293");
        test.setIsActive("N");

        when(externalReferenceDao.getData(labObject.getExternalId(), labObject.getCategory())).thenReturn(reference);
        labTestService.delete(labObject);

        verify(externalReferenceDao).deleteData(reference);
        verify(testDao).deleteById("293", labObject.getSysUserId());
    }
}
